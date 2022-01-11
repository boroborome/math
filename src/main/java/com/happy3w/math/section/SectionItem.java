package com.happy3w.math.section;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class SectionItem <T>{
    public static final String NULL_TEXT = "*";

    private SectionItemValue<T> from;
    private SectionItemValue<T> to;

    public SectionItem(SectionItemValue<T> from, SectionItemValue<T> to) {
        this.from = from;
        this.to = to;
    }

    public SectionItem<T> newCopy() {
        return new SectionItem<>(from, to);
    }

    public void configFrom(T from, boolean includeFrom) {
        this.from.setValue(from);
        this.from.setInclude(includeFrom);
    }

    public void configTo(T to, boolean includeTo) {
        this.to.setValue(to);
        this.to.setInclude(includeTo);
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
        buf.append(from.isInclude() ? '[' : '(')
                .append(from.getValue() == null ? nullFrom : from.getValue().toString())
                .append(',')
                .append(to.getValue() == null ? nullTo : to.getValue().toString())
                .append(to.isInclude() ? ']' : ')');
    }

    public boolean isIncludeTo() {
        return to.isInclude();
    }

    public void setIncludeTo(boolean include) {
        to.setInclude(include);
    }

    public boolean isIncludeFrom() {
        return from.isInclude();
    }

    public void setIncludeFrom(boolean include) {
        from.setInclude(include);
    }

    public T getFromValue() {
        return from.getValue();
    }

    public T getToValue() {
        return to.getValue();
    }

    public static <T> SectionItem<T> ofValue(T from, T to) {
        return new SectionItem<>(new SectionItemValue<>(from, true, ItemValueType.from),
                new SectionItemValue<>(to, true, ItemValueType.to));
    }

    public static <T> SectionItem<T> ofValue(T from, boolean includeFrom, T to, boolean includeTo) {
        return new SectionItem<>(new SectionItemValue<>(from, includeFrom, ItemValueType.from),
                new SectionItemValue<>(to, includeTo, ItemValueType.to));
    }
}
