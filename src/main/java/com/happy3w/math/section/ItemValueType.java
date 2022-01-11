package com.happy3w.math.section;

public enum ItemValueType {
    from(-1),
    to(1);

    private int value;
    ItemValueType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
