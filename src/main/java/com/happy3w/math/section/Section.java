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

    public Section<T> merge(SectionItem<T> newItem) {
        int itemFromIndex = putFromItem(newItem);
        mergeToItem(itemFromIndex, newItem);
        return this;
    }

    public Section<T> merge(Section<T> otherSection) {
        for (SectionItem<T> items : otherSection.items) {
            merge(items);
        }
        return this;
    }

    private void mergeToItem(int itemFromIndex, SectionItem<T> newItem) {
        SectionItem<T> itemToUpdate = items.get(itemFromIndex);
        for (int index = itemFromIndex + 1; index < items.size(); ) {
            SectionItem<T> curItem = items.get(index);
            int crFrom = nullLastComparator.compare(newItem.getTo(), curItem.getFrom());
            if (crFrom < 0) {
                itemToUpdate.configTo(newItem.getTo(), newItem.isIncludeTo());
                return;
            } else if (crFrom == 0) {
                if (curItem.isIncludeFrom() || newItem.isIncludeTo()) {
                    itemToUpdate.configTo(curItem.getTo(), curItem.isIncludeTo());
                    items.remove(index);
                } else {
                    itemToUpdate.configTo(newItem.getTo(), newItem.isIncludeTo());
                }
                return;
            }

            int crTo = nullLastComparator.compare(newItem.getTo(), curItem.getTo());
            if (crTo < 0) {
                itemToUpdate.configTo(curItem.getTo(), curItem.isIncludeTo());
                items.remove(index);
                return;
            } else if (crTo == 0) {
                itemToUpdate.configTo(curItem.getTo(), curItem.isIncludeTo() || newItem.isIncludeTo());
                items.remove(index);
                return;
            }
            items.remove(index);
        }
    }

    private int putFromItem(SectionItem<T> newItem) {
        for (int index = 0; index < items.size(); index++) {
            SectionItem<T> item = items.get(index);
            int crFrom = nullFirstComparator.compare(newItem.getFrom(), item.getFrom());
            if (crFrom < 0) {
                items.add(index, newItem.newCopy());
                return index;
            } else if (crFrom == 0) {
                if (newItem.isIncludeFrom()) {
                    item.setIncludeFrom(true);
                }
                return index;
            }

            int crTo = nullLastComparator.compare(newItem.getFrom(), item.getTo());
            if (crTo < 0) {
                return index;
            } else if (crTo == 0) {
                if (item.isIncludeTo() || newItem.isIncludeTo()) {
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
