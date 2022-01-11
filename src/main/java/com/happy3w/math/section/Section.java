package com.happy3w.math.section;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class Section<T> {
    public static final String SPLITER = "";

    private List<SectionItem<T>> items = new ArrayList<>();
    private SectionItemValueComparator<T> comparator;

    public Section(Comparator<T> comparator, SectionItem<T>... itemArray) {
        this.comparator = new SectionItemValueComparator(comparator);
        items.addAll(Arrays.asList(itemArray));
    }

    public Section<T> unionItem(SectionItem<T> newItem) {
        int itemFromIndex = unionFromItem(newItem);
        unionToItem(itemFromIndex, newItem);

        return this;
    }

    private List<SectionItemValue<T>> allItemValues(List<SectionItem<T>> items, SectionItem<T> newItem) {
        List<SectionItemValue<T>> itemValues = new ArrayList<>();
        for (SectionItem<T> item : items) {
            itemValues.add(item.getFrom());
            itemValues.add(item.getTo());
        }
        itemValues.add(newItem.getFrom());
        itemValues.add(newItem.getTo());

        return itemValues;
    }

    public Section<T> unionSection(Section<T> otherSection) {
        for (SectionItem<T> items : otherSection.items) {
            unionItem(items);
        }
        return this;
    }

    public Section<T> subtract(SectionItem<T> subItem) {
        int itemFromIndex = subFromItem(subItem);
        subToItem(itemFromIndex, subItem);
        return this;
    }

    private void subToItem(int itemFromIndex, SectionItem<T> subItem) {
        if (itemFromIndex >= items.size()) {
            return;
        }

        for (int index = itemFromIndex; index < items.size(); ) {
            SectionItem<T> curItem = items.get(index);
            int crFrom = comparator.compare(subItem.getTo(), curItem.getFrom());
            if (crFrom < 0) {
                return;
            } else if (crFrom == 0) {
                if (subItem.isIncludeTo()) {
                    curItem.setIncludeFrom(false);
                }
                return;
            }

            int crTo = comparator.compare(subItem.getTo(), curItem.getTo());
            if (crTo < 0) {
                curItem.configFrom(subItem.getToValue(), !subItem.isIncludeTo());
                return;
            } else if (crTo == 0) {
                if (!subItem.isIncludeFrom() && curItem.isIncludeFrom()) {
                    curItem.configFrom(subItem.getToValue(), !subItem.isIncludeTo());
                } else {
                    items.remove(index);
                }
                return;
            }
            items.remove(index);
        }
    }

    private int subFromItem(SectionItem<T> subItem) {
        SectionItemValue<T> subItemFrom = subItem.getFrom();
        for (int index = 0; index < items.size(); index++) {
            SectionItem<T> item = items.get(index);
            int crFrom = comparator.compare(subItemFrom, item.getFrom());
            if (crFrom < 0) {
                return index;
            } else if (crFrom == 0) {
                if (item.isIncludeFrom() && !subItemFrom.isInclude()) {
                    items.add(index, SectionItem.ofValue(subItemFrom.getValue(), true, subItemFrom.getValue(), true));
                    return index + 1;
                }
                return index;
            }

            int crTo = comparator.compare(subItemFrom, item.getTo());
            if (crTo < 0) {
                items.add(index + 1, item.newCopy());
                item.setTo(new SectionItemValue<>(subItemFrom.getValue(), !subItemFrom.isInclude(), ItemValueType.to));
                return index + 1;
            } else if (crTo == 0) {
                if (subItemFrom.isInclude()) {
                    item.setIncludeTo(false);
                }
                return index + 1;
            }
        }

        return items.size();
    }

    private void unionToItem(int itemFromIndex, SectionItem<T> newItem) {
        SectionItem<T> itemToUpdate = items.get(itemFromIndex);
        for (int index = itemFromIndex; index < items.size(); ) {
            SectionItem<T> curItem = items.get(index);
            if (index != itemFromIndex) {
                int crFrom = comparator.compare(newItem.getTo(), curItem.getFrom());
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

            int crTo = comparator.compare(newItem.getTo(), curItem.getTo());
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

    private int unionFromItem(SectionItem<T> newItem) {
        for (int index = 0; index < items.size(); index++) {
            SectionItem<T> item = items.get(index);
            int crFrom = comparator.compare(newItem.getFrom(), item.getFrom());
            if (crFrom < 0) {
                items.add(index, newItem.newCopy());
                return index;
            } else if (crFrom == 0) {
                if (newItem.isIncludeFrom()) {
                    item.setIncludeFrom(true);
                }
                return index;
            }

            int crTo = comparator.compare(newItem.getFrom(), item.getTo());
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
