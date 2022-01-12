package com.happy3w.math.section;

public class LongDiscretizeCalculator implements DiscretizeCalculator<Long> {
    @Override
    public Long plus(Long value, long delta) {
        if (value == null) {
            return null;
        }
        return value + delta;
    }
}
