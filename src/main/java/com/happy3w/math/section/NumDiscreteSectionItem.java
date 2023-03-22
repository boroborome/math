package com.happy3w.math.section;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class NumDiscreteSectionItem {
    private Long start;
    private Long end;

    public NumDiscreteSectionItem(int start, int end) {
        this.start = Long.valueOf(start);
        this.end = Long.valueOf(end);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append('[')
                .append(start == null ? "*" : start)
                .append(',')
                .append(end == null ? "*" : end)
                .append(']')
                .toString();
    }
}
