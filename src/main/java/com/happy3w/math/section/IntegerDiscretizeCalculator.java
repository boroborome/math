package com.happy3w.math.section;

public class IntegerDiscretizeCalculator implements DiscretizeCalculator<Integer> {
    @Override
    public Integer plus(Integer value, long delta) {
        if (value == null) {
            return null;
        }
        return value + (int) delta;
    }
}
