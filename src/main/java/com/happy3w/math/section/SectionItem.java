package com.happy3w.math.section;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class SectionItem <T>{
    public static final String NULL_TEXT = "*";

    private T from;
    private boolean includeFrom;
    private T to;
    private boolean includeTo;

    public SectionItem(T from, T to) {
        this(from, true, to, true);
    }

    public SectionItem(T from, boolean includeFrom, T to, boolean includeTo) {
        this.from = from;
        this.includeFrom = from == null ? false : includeFrom;
        this.to = to;
        this.includeTo = to == null ? false : includeTo;
    }

    public SectionItem<T> newCopy() {
        return new SectionItem<>(from, includeFrom, to, includeTo);
    }

    public void configFrom(T from, boolean includeFrom) {
        this.from = from;
        this.includeFrom = includeFrom;
    }

    public void configTo(T to, boolean includeTo) {
        this.to = to;
        this.includeTo = includeTo;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        try {
            output(buf, NULL_TEXT, NULL_TEXT);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected exception.", e);
        }
        return buf.toString();
    }

    public void output(Appendable buf, String nullFrom, String nullTo) throws IOException {
        buf.append(includeFrom ? '[' : '(')
                .append(from == null ? nullFrom : from.toString())
                .append(',')
                .append(to == null ? nullTo : to.toString())
                .append(includeTo ? ']' : ')');
    }
}
