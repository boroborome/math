package com.happy3w.math.section;

import java.util.Comparator;

public class SectionItemValueComparator<T> {
    private final Comparator<T> valueComparator;

    public SectionItemValueComparator(Comparator<T> valueComparator) {
        this.valueComparator = valueComparator;
    }

    public int compareValue(T value, SectionItemValue<T> o1, ItemValueType type1) {
        int kind1 = getValueKind(o1, type1);
        if (kind1 != 0) {
            return -kind1;
        }
        int crValue = valueComparator.compare(value, o1.getValue());
        if (crValue == 0 && !o1.isInclude()) {
            return -type1.getValue();
        }
        return crValue;
    }

    public int compare(SectionItemValue<T> o1, ItemValueType type1, SectionItemValue<T> o2, ItemValueType type2) {
        int kind1 = getValueKind(o1, type1);
        int kind2 = getValueKind(o2, type2);
        int crKind = kind1 - kind2;
        if (crKind != 0 || kind1 != 0) {
            return crKind;
        }

        return valueComparator.compare(o1.getValue(), o2.getValue());
    }

    private int getValueKind(SectionItemValue<T> itemValue, ItemValueType type) {
        if (itemValue.getValue() != null) {
            return 0;
        } else if (type == ItemValueType.from) {
            return -1;
        } else {
            return 1;
        }
    }
}
