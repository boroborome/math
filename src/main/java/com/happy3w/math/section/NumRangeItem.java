package com.happy3w.math.section;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class NumRangeItem {
    private Long start;
    private Long end;

    public NumRangeItem(int start, int end) {
        this.start = Long.valueOf(start);
        this.end = Long.valueOf(end);
    }

    public void output(StringBuilder buf) {
        if (start == null) {
            buf.append("(*");
        } else {
            buf.append('[')
                    .append(start);
        }
        buf.append(',');
        if (end == null) {
            buf.append("*)");
        } else {
            buf.append(end)
                    .append(']');
        }
    }
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        output(buf);
        return buf.toString();
    }
}
