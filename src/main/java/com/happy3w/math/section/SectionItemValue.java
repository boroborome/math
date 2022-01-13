package com.happy3w.math.section;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Getter
@Setter
@NoArgsConstructor
public class SectionItemValue<T> {
    private T value;
    private boolean include;

    public SectionItemValue(T value, boolean include) {
        this.value = value;
        this.include = value == null ? false : include;
    }

    @Override
    public String toString() {
        return value + "," + include;
    }

    public static <T> Stream<T> streamWithStartEnd(
            SectionItemValue<T> start, SectionItemValue<T> end, int step,
            DiscretizeCalculator<T> discretizeCalculator, Comparator<T> valueComparator) {
        if (start.getValue() == null) {
            throw new IllegalArgumentException("Can not start stream with min value null");
        }

        T seed = start.isInclude() ? start.getValue() : discretizeCalculator.plus(start.getValue(), step);
        T endValue = end.getValue();
        if (endValue != null && !end.isInclude()) {
            endValue = discretizeCalculator.plus(end.getValue(), -step);
        }

        return streamWithStartEndValue(seed, endValue, step, discretizeCalculator, valueComparator);
    }

    public static <T> Stream<T> streamWithStartEndValue(
            T start, T end, int step,
            DiscretizeCalculator<T> discretizeCalculator, Comparator<T> valueComparator) {
        return StreamSupport.stream(
                new StartEndStream(start, end, step, discretizeCalculator, valueComparator),
                false);
    }

    public static class StartEndStream<T> extends Spliterators.AbstractSpliterator<T> {
        private T start;
        private T end;
        private int step;
        private DiscretizeCalculator<T> discretizeCalculator;
        private Comparator<T> valueComparator;

        private boolean isInit = false;
        private T preValue;

        public StartEndStream(T start, T end, int step,
                                 DiscretizeCalculator<T> discretizeCalculator, Comparator<T> valueComparator) {
            super(Long.MAX_VALUE, 0);
            this.start = start;
            this.end = end;
            this.step = step;
            this.discretizeCalculator = discretizeCalculator;
            this.valueComparator = valueComparator;
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if (!isInit) {
                isInit = true;
                preValue = start;
                action.accept(preValue);
                return true;
            }

            T newValue = discretizeCalculator.plus(preValue, step);
            preValue = newValue;
            if (end == null) {
                action.accept(newValue);
                return true;
            }
            int cr = valueComparator.compare(newValue, end);
            if (cr * step <= 0) {
                action.accept(newValue);
                return true;
            }
            return false;
        }
    }
}
