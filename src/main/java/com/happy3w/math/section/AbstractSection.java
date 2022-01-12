package com.happy3w.math.section;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
public abstract class AbstractSection<T, S extends AbstractSection<T, S>> {
    public static final String SPLITER = "";

    protected List<SectionItem<T>> items = new ArrayList<>();
    protected Comparator<T> valueComparator;
    protected SectionItemValueComparator<T> itemValueComparator;

    public AbstractSection(SectionItem<T>... itemArray) {
        init((Comparator<T>) Comparator.comparing(Function.identity()), itemArray);
    }

    public AbstractSection(Class<T> type, SectionItem<T>... itemArray) {
        if (!Comparable.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException("Param type should be Comparable");
        }
        init((Comparator<T>) Comparator.comparing(Function.identity()), itemArray);
    }

    public AbstractSection(Comparator<T> itemValueComparator, SectionItem<T>... itemArray) {
        init(itemValueComparator, itemArray);
    }

    private void init(Comparator<T> comparator, SectionItem<T>... itemArray) {
        this.valueComparator = comparator;
        this.itemValueComparator = new SectionItemValueComparator(comparator);
        items.addAll(Arrays.asList(itemArray));
    }

    public S unionSection(S otherSection) {
        for (SectionItem<T> items : otherSection.items) {
            unionItem(items);
        }
        return (S) this;
    }

    public S unionItem(SectionItem<T> newItem) {
        int itemFromIndex = unionFromItem(newItem);
        unionToItem(itemFromIndex, newItem);

        return (S) this;
    }

    public S subtractSection(S otherSection) {
        for (SectionItem<T> items : otherSection.items) {
            subtractItem(items);
        }
        return (S) this;
    }

    public S subtractItem(SectionItem<T> subItem) {
        int itemFromIndex = subFromItem(subItem);
        subToItem(itemFromIndex, subItem);
        return (S) this;
    }

    protected abstract SectionItemValue<T> removePoint(SectionItemValue<T> itemValue, ItemValueType type);

    protected void subToItem(int itemFromIndex, SectionItem<T> subItem) {
        if (itemFromIndex >= items.size()) {
            return;
        }

        for (int index = itemFromIndex; index < items.size(); ) {
            SectionItem<T> curItem = items.get(index);
            int crFrom = itemValueComparator.compare(subItem.getTo(), ItemValueType.to, curItem.getFrom(), ItemValueType.from);
            if (crFrom < 0) {
                return;
            } else if (crFrom == 0) {
                if (subItem.isIncludeTo()) {
                    SectionItemValue<T> orgFrom = curItem.getFrom();
                    curItem.setFrom(newItemValue(orgFrom.getValue(), !orgFrom.isInclude(), ItemValueType.from));
                }
                return;
            }

            int crTo = itemValueComparator.compare(subItem.getTo(), ItemValueType.to, curItem.getTo(), ItemValueType.to);
            if (crTo < 0) {
                curItem.setFrom(newItemValue(subItem.getToValue(), !subItem.isIncludeTo(), ItemValueType.from));
                return;
            } else if (crTo == 0) {
                if (!subItem.isIncludeFrom() && curItem.isIncludeFrom()) {
                    curItem.setFrom(newItemValue(subItem.getToValue(), !subItem.isIncludeTo(), ItemValueType.from));
                } else {
                    items.remove(index);
                }
                return;
            }
            items.remove(index);
        }
    }

    protected SectionItemValue<T> newItemValue(T value, boolean include, ItemValueType type) {
        SectionItemValue<T> newItemValue = new SectionItemValue<>(value, include);
        if (!include) {
            newItemValue = removePoint(newItemValue, type);
        }
        return newItemValue;
    }

    protected int subFromItem(SectionItem<T> subItem) {
        SectionItemValue<T> subItemFrom = subItem.getFrom();
        for (int index = 0; index < items.size(); index++) {
            SectionItem<T> item = items.get(index);
            int crFrom = itemValueComparator.compare(subItemFrom, ItemValueType.from, item.getFrom(), ItemValueType.from);
            if (crFrom < 0) {
                return index;
            } else if (crFrom == 0) {
                if (item.isIncludeFrom() && !subItemFrom.isInclude()) {
                    items.add(index, SectionItem.ofValue(subItemFrom.getValue(), true, subItemFrom.getValue(), true));
                    return index + 1;
                }
                return index;
            }

            int crTo = itemValueComparator.compare(subItemFrom, ItemValueType.from, item.getTo(), ItemValueType.to);
            if (crTo < 0) {
                items.add(index + 1, item.newCopy());
                item.setTo(newItemValue(subItemFrom.getValue(), !subItemFrom.isInclude(), ItemValueType.to));
                return index + 1;
            } else if (crTo == 0) {
                if (subItemFrom.isInclude()) {
                    item.setTo(newItemValue(item.getTo().getValue(), false, ItemValueType.to));
                }
                return index + 1;
            }
        }

        return items.size();
    }

    /**
     *
     * @param newItem
     * @return return where from is set in
     */
    protected int unionFromItem(SectionItem<T> newItem) {
        for (int index = 0; index < items.size(); index++) {
            SectionItem<T> item = items.get(index);
            int crFrom = itemValueComparator.compare(newItem.getFrom(), ItemValueType.from, item.getFrom(), ItemValueType.from);
            if (crFrom < 0) {
                items.add(index, newItem.newCopy());
                return index;
            } else if (crFrom == 0) {
                if (newItem.isIncludeFrom()) {
                    item.setIncludeFrom(true);
                }
                return index;
            }

            int crTo = itemValueComparator.compare(newItem.getFrom(), ItemValueType.from, item.getTo(), ItemValueType.to);
            if (crTo < 0) {
                return index;
            } else if (crTo == 0) {
                if (item.isIncludeTo() || newItem.isIncludeFrom()) {
                    return index;
                } else {
                    int newIndex = index + 1;
                    items.add(newIndex, newItem.newCopy());
                    return newIndex;
                }
            }
        }

        items.add(newItem.newCopy());
        return items.size() - 1;
    }

    protected void unionToItem(int itemFromIndex, SectionItem<T> newItem) {
        SectionItem<T> itemToUpdate = items.get(itemFromIndex);
        for (int index = itemFromIndex; index < items.size(); ) {
            SectionItem<T> curItem = items.get(index);
            if (index != itemFromIndex) {
                int crFrom = itemValueComparator.compare(newItem.getTo(), ItemValueType.to, curItem.getFrom(), ItemValueType.from);
                if (crFrom < 0) {
                    itemToUpdate.configTo(newItem.getToValue(), newItem.isIncludeTo());
                    return;
                } else if (crFrom == 0) {
                    if (curItem.isIncludeFrom() || newItem.isIncludeTo()) {
                        itemToUpdate.configTo(curItem.getToValue(), curItem.isIncludeTo());
                        items.remove(index);
                    } else {
                        itemToUpdate.configTo(newItem.getToValue(), newItem.isIncludeTo());
                    }
                    return;
                }
            }

            int crTo = itemValueComparator.compare(newItem.getTo(), ItemValueType.to, curItem.getTo(), ItemValueType.to);
            if (crTo < 0) {
                if (itemToUpdate != curItem) {
                    itemToUpdate.configTo(curItem.getToValue(), curItem.isIncludeTo());
                    items.remove(index);
                }
                return;
            }
            if (crTo == 0) {
                itemToUpdate.configTo(curItem.getToValue(), curItem.isIncludeTo() || newItem.isIncludeTo());
            }
            if (itemToUpdate != curItem) {
                items.remove(index);
            } else {
                index++;
            }
        }
        itemToUpdate.configTo(newItem.getToValue(), newItem.isIncludeTo());
    }


    @Override
    public String toString() {
        return formatText(SectionItem.NULL_TEXT, SectionItem.NULL_TEXT, SPLITER);
    }

    public String formatText(String nullFrom, String nullTo, String spliter) {
        StringBuilder buf = new StringBuilder();
        try {
            for (SectionItem<T> item : items) {
                if (buf.length() > 0) {
                    buf.append(spliter);
                }
                item.output(buf, nullFrom, nullTo);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unexpected error.", e);
        }
        return buf.toString();
    }
}
