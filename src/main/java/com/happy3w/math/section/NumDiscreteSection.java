package com.happy3w.math.section;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@NoArgsConstructor
public class NumDiscreteSection {
    private List<NumDiscreteSectionItem> items = new ArrayList<>();

    public NumDiscreteSection(List<NumDiscreteSectionItem> items) {
        unionItems(items);
    }

    private void installEnd(Long end, int startIndex, List<NumDiscreteSectionItem> items) {
        for (int index = startIndex; index < items.size(); ) {
            NumDiscreteSectionItem item = items.get(index);
            Long itemEnd = item.getEnd();
            long endGap = gap(end, itemEnd, false);
            if (endGap <= 0) {
                return;
            }
            item.setEnd(end);

            int nextIndex = index + 1;
            if (nextIndex >= items.size()) {
                return;
            }
            NumDiscreteSectionItem nextItem = items.get(nextIndex);
            Long itemStart = nextItem.getStart();
            long startGap = gap(end, itemStart, false);
            if (startGap < -1) {
                return;
            } else {
                items.remove(nextIndex);
                item.setEnd(nextItem.getEnd());
            }
        }
    }

    private long gap(Long a, Long b, boolean nullIsMin) {
        if (a == b) {
            return 0;
        }

        if (a == null) {
            return nullIsMin ? -10 : 10;
        }

        if (b == null) {
            return nullIsMin ? 10 : -10;
        }

        return a - b;
    }

    private int installStart(Long start, Long end, List<NumDiscreteSectionItem> items) {
        for (int index = 0; index < items.size(); index++) {
            NumDiscreteSectionItem item = items.get(index);
            Long itemStart = item.getStart();
            long startGap = gap(start, itemStart, true);
            if (startGap < -1) {
                NumDiscreteSectionItem newItem = new NumDiscreteSectionItem(start, start);
                items.add(index, newItem);
                return index;
            } else if (startGap < 1) {
                item.setStart(start);
                return index;
            } else if (startGap < 2) {
                return index;
            }

            Long itemEnd = item.getEnd();
            long endGap = gap(start, itemEnd, false);
            if (endGap <= 1) {
                return index;
            } else {
                continue;
            }
        }

        NumDiscreteSectionItem newItem = new NumDiscreteSectionItem(start, end);
        items.add(newItem);
        return items.size() - 1;
    }

    public Stream<NumDiscreteSectionItem> items() {
        return items.stream();
    }

    public NumDiscreteSection unionSection(NumDiscreteSection otherSection) {
        return unionItems(otherSection.items);
    }

    public NumDiscreteSection unionItems(List<NumDiscreteSectionItem> newItems) {
        if (newItems != null) {
            for (NumDiscreteSectionItem item : newItems) {
                unionItem(item);
            }
        }
        return this;
    }

    public NumDiscreteSection unionItem(NumDiscreteSectionItem item) {
        int index = installStart(item.getStart(), item.getEnd(), items);
        installEnd(item.getEnd(), index, items);
        return this;
    }

    public NumDiscreteSection unionValue(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("value added into section should not be null for confused meaning.");
        }
        return unionItem(new NumDiscreteSectionItem(value, value));
    }
}
