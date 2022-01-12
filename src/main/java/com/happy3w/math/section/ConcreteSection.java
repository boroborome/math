package com.happy3w.math.section;

import java.util.Comparator;

public class ConcreteSection<T> extends AbstractSection<T, ConcreteSection<T>> {

    public ConcreteSection(SectionItem<T>... itemArray) {
        super(itemArray);
    }

    public ConcreteSection(Class<T> type, SectionItem<T>... itemArray) {
        super(type, itemArray);
    }

    public ConcreteSection(Comparator<T> comparator, SectionItem<T>... itemArray) {
        super(comparator, itemArray);
    }

    @Override
    protected SectionItemValue<T> removePoint(SectionItemValue<T> itemValue, ItemValueType type) {
        itemValue.setInclude(false);
        return itemValue;
    }
}
