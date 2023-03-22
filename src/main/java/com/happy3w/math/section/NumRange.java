package com.happy3w.math.section;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@NoArgsConstructor
public class NumRange {
    private List<NumRangeItem> items = new ArrayList<>();

    public NumRange(List<NumRangeItem> items) {
        unionItems(items);
    }

    private void installEnd(Long end, int startIndex, List<NumRangeItem> items) {
        for (int index = startIndex; index < items.size(); ) {
            NumRangeItem item = items.get(index);
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
            NumRangeItem nextItem = items.get(nextIndex);
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

    private int installStart(Long start, Long end, List<NumRangeItem> items) {
        for (int index = 0; index < items.size(); index++) {
            NumRangeItem item = items.get(index);
            Long itemStart = item.getStart();
            long startGap = gap(start, itemStart, true);
            if (startGap < -1) {
                NumRangeItem newItem = new NumRangeItem(start, start);
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

        NumRangeItem newItem = new NumRangeItem(start, end);
        items.add(newItem);
        return items.size() - 1;
    }

    public Stream<NumRangeItem> items() {
        return items.stream();
    }

    public NumRange unionRange(NumRange otherSection) {
        return unionItems(otherSection.items);
    }

    public NumRange unionItems(List<NumRangeItem> newItems) {
        if (newItems != null) {
            for (NumRangeItem item : newItems) {
                unionItem(item);
            }
        }
        return this;
    }

    public NumRange unionItem(NumRangeItem item) {
        int index = installStart(item.getStart(), item.getEnd(), items);
        installEnd(item.getEnd(), index, items);
        return this;
    }

    public NumRange unionValue(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("value added into section should not be null for confused meaning.");
        }
        return unionItem(new NumRangeItem(value, value));
    }

    public NumRange subtractRange(NumRange otherSection) {
        return subtractItems(otherSection.items);
    }

    public NumRange subtractItems(List<NumRangeItem> newItems) {
        if (newItems != null) {
            for (NumRangeItem item : newItems) {
                subtractItem(item);
            }
        }
        return this;
    }

    public NumRange subtractItem(NumRangeItem item) {
        int index = subtractStart(item.getStart(), items);
        if (index >= 0) {
            subtractEnd(item.getEnd(), index, items);
        }

        return this;
    }

    private void subtractEnd(Long end, int startIndex, List<NumRangeItem> items) {
        for (int index = startIndex; index < items.size(); ) {
            NumRangeItem item = items.get(index);
            Long itemStart = item.getStart();
            long gapStart = gap(end, itemStart, false);
            if (gapStart < 0) {
                return;
            }
            item.setStart(end + 1);

            Long itemEnd = item.getEnd();
            long gapEnd = gap(end, itemEnd, false);
            if (gapEnd < 0) {
                return;
            }
            items.remove(index);
        }
    }

    private int subtractStart(Long start, List<NumRangeItem> items) {
        for (int index = 0; index < items.size(); index++) {
            NumRangeItem item = items.get(index);
            Long itemStart = item.getStart();
            long gapStart = gap(start, itemStart, true);
            if (gapStart <= 0) {
                return index;
            }

            Long itemEnd = item.getEnd();
            long gapEnd = gap(start, itemEnd, false);
            if (gapEnd <= 0) {
                int nextIndex = index + 1;
                items.add(nextIndex, new NumRangeItem(start + 1, item.getEnd()));
                item.setEnd(start - 1);
                return nextIndex;
            }
        }
        return -1;
    }

    public Long size() {
        long size = 0;
        for (NumRangeItem item : items) {
            if (item.getEnd() == null || item.getStart() == null) {
                return null;
            }
            size += (item.getEnd() - item.getStart() + 1);
        }
        return size;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (NumRangeItem item : items) {
            if (buf .length() > 0) {
                buf.append(';');
            }
            item.output(buf);
        }
        return buf.toString();
    }
}
