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

        SectionItemValue<T> subItemTo = subItem.getTo();
        for (int index = itemFromIndex; index < items.size(); ) {
            SectionItem<T> curItem = items.get(index);

            SectionItemValue<T> curItemFrom = curItem.getFrom();
            int crFrom = itemValueComparator.compare(subItemTo, ItemValueType.to, curItemFrom, ItemValueType.from);
            if (crFrom < 0) {
                return;
            } else if (crFrom == 0) {
                if (subItemTo.isInclude()) {
                    curItem.setFrom(newItemValue(curItemFrom.getValue(), !curItemFrom.isInclude(), ItemValueType.from));
                }
                return;
            }

            SectionItemValue<T> curItemTo = curItem.getTo();
            int crTo = itemValueComparator.compare(subItemTo, ItemValueType.to, curItemTo, ItemValueType.to);
            if (crTo < 0) {
                curItem.setFrom(newItemValue(subItemTo.getValue(), !subItemTo.isInclude(), ItemValueType.from));
                return;
            } else if (crTo == 0) {
                if (!subItemTo.isInclude() && curItemTo.isInclude()) {
                    curItem.setFrom(newItemValue(subItemTo.getValue(), !subItemTo.isInclude(), ItemValueType.from));
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
            SectionItemValue<T> itemFrom = item.getFrom();
            int crFrom = itemValueComparator.compare(subItemFrom, ItemValueType.from, itemFrom, ItemValueType.from);
            if (crFrom < 0) {
                return index;
            } else if (crFrom == 0) {
                if (itemFrom.isInclude() && !subItemFrom.isInclude()) {
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
     * @param newItem item to be union
     * @return return where from is set in
     */
    protected int unionFromItem(SectionItem<T> newItem) {
        SectionItemValue<T> newItemFrom = newItem.getFrom();
        for (int index = 0; index < items.size(); index++) {
            SectionItem<T> item = items.get(index);
            SectionItemValue<T> itemFrom = item.getFrom();
            int crFrom = itemValueComparator.compare(newItemFrom, ItemValueType.from, itemFrom, ItemValueType.from);
            if (crFrom < 0) {
                items.add(index, newItem.newCopy());
                return index;
            } else if (crFrom == 0) {
                if (newItemFrom.isInclude()) {
                    itemFrom.setInclude(true);
                }
                return index;
            }

            SectionItemValue<T> itemTo = item.getTo();
            int crTo = itemValueComparator.compare(newItemFrom, ItemValueType.from, itemTo, ItemValueType.to);
            if (crTo < 0) {
                return index;
            } else if (crTo == 0) {
                if (itemTo.isInclude() || newItemFrom.isInclude()) {
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
        SectionItemValue<T> newItemTo = newItem.getTo();
        for (int index = itemFromIndex; index < items.size(); ) {
            SectionItem<T> curItem = items.get(index);
            SectionItemValue<T> curItemFrom = curItem.getFrom();
            SectionItemValue<T> curItemTo = curItem.getTo();
            if (index != itemFromIndex) {
                int crFrom = itemValueComparator.compare(newItemTo, ItemValueType.to, curItemFrom, ItemValueType.from);
                if (crFrom < 0) {
                    itemToUpdate.configTo(newItemTo.getValue(), newItemTo.isInclude());
                    return;
                } else if (crFrom == 0) {
                    if (curItemFrom.isInclude() || newItemTo.isInclude()) {
                        itemToUpdate.configTo(curItemTo.getValue(), curItemTo.isInclude());
                        items.remove(index);
                    } else {
                        itemToUpdate.configTo(newItemTo.getValue(), newItemTo.isInclude());
                    }
                    return;
                }
            }

            int crTo = itemValueComparator.compare(newItemTo, ItemValueType.to, curItemTo, ItemValueType.to);
            if (crTo < 0) {
                if (itemToUpdate != curItem) {
                    itemToUpdate.configTo(curItemTo.getValue(), curItemTo.isInclude());
                    items.remove(index);
                }
                return;
            }
            if (crTo == 0) {
                itemToUpdate.configTo(curItemTo.getValue(), curItemTo.isInclude() || newItemTo.isInclude());
            }
            if (itemToUpdate != curItem) {
                items.remove(index);
            } else {
                index++;
            }
        }
        itemToUpdate.configTo(newItemTo.getValue(), newItemTo.isInclude());
    }

    public abstract S newEmptySection();

    public S reverse() {
        return newEmptySection()
                .unionItem(SectionItem.ofValue(null, null))
                .subtractSection((S) this);
    }

    public boolean contains(T value) {
        if (value == null) {
            return false;
        }

        for (SectionItem<T> item : items) {
            int crFrom = itemValueComparator.compareValue(value, item.getFrom(), ItemValueType.from);
            if (crFrom == 0) {
                return true;
            }

            int crTo = itemValueComparator.compareValue(value, item.getTo(), ItemValueType.to);

            if (crFrom >= 0 && crTo <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return formatText(Object::toString, SectionItem.NULL_TEXT, SectionItem.NULL_TEXT, SPLITER);
    }

    public String formatText(Function<T, String> valueFormatter, String nullFrom, String nullTo, String spliter) {
        StringBuilder buf = new StringBuilder();
        try {
            for (SectionItem<T> item : items) {
                if (buf.length() > 0) {
                    buf.append(spliter);
                }
                item.output(buf, valueFormatter, nullFrom, nullTo);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unexpected error.", e);
        }
        return buf.toString();
    }
}
