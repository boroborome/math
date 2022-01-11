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

        int crValue = valueComparator.compare(o1.getValue(), o2.getValue());
        if (crValue != 0) {
            return crValue;
        }

        int crType = o1.getType().getValue() - o2.getType().getValue();
        if (crType != 0) {
            return crType;
        }

        int includeV1 = o1.isInclude() ? 1 : 0;
        int includeV2 = o2.isInclude() ? 1 : 0;

        return (includeV1 - includeV2) * o1.getType().getValue();
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
