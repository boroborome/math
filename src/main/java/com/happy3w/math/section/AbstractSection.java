package com.happy3w.math.section;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
public abstract class AbstractSection<T, S extends AbstractSection<T, S>> {
    public static final String SPLITER = "";

    protected List<SectionItem<T>> items = new ArrayList<>();
    protected SectionItemValueComparator<T> comparator;

    public AbstractSection(Class<T> type, SectionItem<T>... itemArray) {
        if (!Comparable.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException("Param type should be Comparable");
        }
        this.comparator = new SectionItemValueComparator(Comparator.comparing(Function.identity()));
        items.addAll(Arrays.asList(itemArray));
    }

    public AbstractSection(Comparator<T> comparator, SectionItem<T>... itemArray) {
        this.comparator = new SectionItemValueComparator(comparator);
        items.addAll(Arrays.asList(itemArray));
    }

    public abstract S unionItem(SectionItem<T> newItem);

    public S unionSection(S otherSection) {
        for (SectionItem<T> items : otherSection.items) {
            unionItem(items);
        }
        return (S) this;
    }

    public abstract S subtract(SectionItem<T> subItem);
}
