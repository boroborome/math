package com.happy3w.math.section;

import java.util.Comparator;

public class SectionItemValueComparator<T> implements Comparator<SectionItemValue<T>> {
    private final Comparator<T> valueComparator;

    public SectionItemValueComparator(Comparator<T> valueComparator) {
        this.valueComparator = valueComparator;
    }

    @Override
    public int compare(SectionItemValue<T> o1, SectionItemValue<T> o2) {
        int kind1 = getValueKind(o1);
        int kind2 = getValueKind(o2);
        int crKind = kind1 - kind2;
        if (crKind != 0 || kind1 != 0) {
            return crKind;
        }

        return valueComparator.compare(o1.getValue(), o2.getValue());
    }

    private int getValueKind(SectionItemValue<T> itemValue) {
        if (itemValue.getValue() != null) {
            return 0;
        } else if (itemValue.getType() == ItemValueType.from) {
            return -1;
        } else {
            return 1;
        }
    }
}
