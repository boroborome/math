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
    private Comparator<T> comparator;
    private Comparator<T> nullFirstComparator;
    private Comparator<T> nullLastComparator;

    public Section(Comparator<T> comparator, SectionItem<T>... itemArray) {
        this.comparator = comparator;
        this.nullFirstComparator = Comparator.nullsFirst(comparator);
        this.nullLastComparator = Comparator.nullsLast(comparator);
        items.addAll(Arrays.asList(itemArray));
    }

    public Section<T> unionItem(SectionItem<T> newItem) {
        int itemFromIndex = unionFromItem(newItem);
        unionToItem(itemFromIndex, newItem);
        return this;
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
            int crFrom = nullLastComparator.compare(subItem.getToValue(), curItem.getFromValue());
            if (crFrom < 0) {
                return;
            } else if (crFrom == 0) {
                if (subItem.isIncludeTo()) {
                    curItem.setIncludeFrom(false);
                }
                return;
            }

            int crTo = nullLastComparator.compare(subItem.getToValue(), curItem.getToValue());
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
        for (int index = 0; index < items.size(); index++) {
            SectionItem<T> item = items.get(index);
            int crFrom = nullFirstComparator.compare(subItem.getFromValue(), item.getFromValue());
            if (crFrom < 0) {
                return index;
            } else if (crFrom == 0) {
                if (item.isIncludeFrom() && !subItem.isIncludeFrom()) {
                    items.add(index, SectionItem.ofValue(item.getFromValue(), true, item.getFromValue(), true));
                    return index + 1;
                }
                return index;
            }

            int crTo = nullLastComparator.compare(subItem.getFromValue(), item.getToValue());
            if (crTo < 0) {
                items.add(index + 1, item.newCopy());
                item.configTo(subItem.getFromValue(), !subItem.isIncludeFrom());
                return index + 1;
            } else if (crTo == 0) {
                if (subItem.isIncludeFrom()) {
                    item.setIncludeTo(false);
                }
                return index + 1;
            }
        }

        return items.size();
    }

    private void unionToItem(int itemFromIndex, SectionItem<T> newItem) {
        SectionItem<T> itemToUpdate = items.get(itemFromIndex);
        for (int index = itemFromIndex + 1; index < items.size(); ) {
            SectionItem<T> curItem = items.get(index);
            int crFrom = nullLastComparator.compare(newItem.getToValue(), curItem.getFromValue());
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

            int crTo = nullLastComparator.compare(newItem.getToValue(), curItem.getToValue());
            if (crTo < 0) {
                itemToUpdate.configTo(curItem.getToValue(), curItem.isIncludeTo());
                items.remove(index);
                return;
            } else if (crTo == 0) {
                itemToUpdate.configTo(curItem.getToValue(), curItem.isIncludeTo() || newItem.isIncludeTo());
                items.remove(index);
                return;
            }
            items.remove(index);
        }
        itemToUpdate.configTo(newItem.getToValue(), newItem.isIncludeTo());
    }

    private int unionFromItem(SectionItem<T> newItem) {
        for (int index = 0; index < items.size(); index++) {
            SectionItem<T> item = items.get(index);
            int crFrom = nullFirstComparator.compare(newItem.getFromValue(), item.getFromValue());
            if (crFrom < 0) {
                items.add(index, newItem.newCopy());
                return index;
            } else if (crFrom == 0) {
                if (newItem.isIncludeFrom()) {
                    item.setIncludeFrom(true);
                }
                return index;
            }

            int crTo = nullLastComparator.compare(newItem.getFromValue(), item.getToValue());
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
