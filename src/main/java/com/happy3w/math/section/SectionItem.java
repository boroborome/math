package com.happy3w.math.section;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.Stream;

@Getter
@Setter
public class SectionItem<T> {
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
        this.from = new SectionItemValue<>(from, includeFrom);
    }

    public void configTo(T to, boolean includeTo) {
        this.to = new SectionItemValue<>(to, includeTo);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        try {
            output(buf, Object::toString, NULL_TEXT, NULL_TEXT);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected exception.", e);
        }
        return buf.toString();
    }

    public void output(Appendable buf, Function<T, String> valueFormatter, String nullFrom, String nullTo) throws IOException {
        buf.append(from.isInclude() ? '[' : '(')
                .append(from.getValue() == null ? nullFrom : valueFormatter.apply(from.getValue()))
                .append(',')
                .append(to.getValue() == null ? nullTo : valueFormatter.apply(to.getValue()))
                .append(to.isInclude() ? ']' : ')');
    }

    public static <T> SectionItem<T> ofValue(T from, T to) {
        return new SectionItem<>(new SectionItemValue<>(from, true),
                new SectionItemValue<>(to, true));
    }

    public static <T> SectionItem<T> ofValue(T from, boolean includeFrom, T to, boolean includeTo) {
        return new SectionItem<>(new SectionItemValue<>(from, includeFrom),
                new SectionItemValue<>(to, includeTo));
    }

    public Stream<T> stream(DiscretizeCalculator<T> discretizeCalculator, Comparator<T> valueComparator) {
        if (from.getValue() == null && to.getValue() == null) {
            throw new IllegalArgumentException("Can not stream SectionItem with both null.");
        }
        if (from.getValue() != null) {
            return streamAsc(discretizeCalculator, valueComparator);
        } else {
            return streamDesc(discretizeCalculator, valueComparator);
        }
    }

    public Stream<T> streamAsc(DiscretizeCalculator<T> discretizeCalculator, Comparator<T> valueComparator) {
        return SectionItemValue.streamWithStartEnd(from, to, 1, discretizeCalculator, valueComparator);
    }

    public Stream<T> streamDesc(DiscretizeCalculator<T> discretizeCalculator, Comparator<T> valueComparator) {
        return SectionItemValue.streamWithStartEnd(to, from, -1, discretizeCalculator, valueComparator);
    }

}
