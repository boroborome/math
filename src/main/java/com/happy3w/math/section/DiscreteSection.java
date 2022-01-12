package com.happy3w.math.section;

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class DiscreteSection<T> extends AbstractSection<T, DiscreteSection<T>> {
    private DiscretizeCalculator<T> discretizeCalculator;

    public DiscreteSection(DiscretizeCalculator<T> discretizeCalculator, SectionItem<T>... itemArray) {
        super(itemArray);
        this.discretizeCalculator = discretizeCalculator;
        discretizeItems(items);
    }

    public DiscreteSection(DiscretizeCalculator<T> discretizeCalculator, Comparator<T> comparator, SectionItem<T>... itemArray) {
        super(comparator, itemArray);
        this.discretizeCalculator = discretizeCalculator;
        discretizeItems(items);
    }

    @Override
    public DiscreteSection<T> unionItem(SectionItem<T> item) {
        SectionItem<T> newItem = discretizeItem(item);
        int itemFromIndex = unionFromItem(newItem);
        int newIndex = tryMerge(itemFromIndex);

        unionToItem(newIndex, newItem);
        tryMerge(newIndex + 1);
        return this;
    }

    private int tryMerge(int itemIndex) {
        if (itemIndex > 0 && itemIndex < items.size()) {
            SectionItem<T> preItem = items.get(itemIndex - 1);
            SectionItem<T> curItem = items.get(itemIndex);

            T newValue = discretizeCalculator.plus(preItem.getTo().getValue(), 1);
            if (valueComparator.compare(newValue, curItem.getFrom().getValue()) == 0) {
                preItem.setTo(curItem.getTo());
                items.remove(itemIndex);
                return itemIndex - 1;
            }
        }
        return itemIndex;
    }

    @Override
    public DiscreteSection<T> subtract(SectionItem<T> subItem) {
        SectionItem<T> item = discretizeItem(subItem);
        return super.subtract(item);
    }

    protected void discretizeItems(List<SectionItem<T>> items) {
        for (int i = 0; i < items.size(); i++) {
            SectionItem<T> item = items.get(i);
            SectionItem<T> newItem = discretizeItem(item);
            items.set(i, newItem);
        }
    }

    protected SectionItem<T> discretizeItem(SectionItem<T> item) {
        SectionItemValue<T> from = discretizeValue(item.getFrom(), ItemValueType.from);
        SectionItemValue<T> to = discretizeValue(item.getTo(), ItemValueType.to);
        if (item.getTo() != to || item.getFrom() != from) {
            return new SectionItem<>(from, to);
        }
        return item;
    }

    protected SectionItemValue<T> discretizeValue(SectionItemValue<T> value, ItemValueType type) {
        if (value.isInclude() || value.getValue() == null) {
            return value;
        }
        long delta = type == ItemValueType.from ? 1 : -1;
        return new SectionItemValue<>(discretizeCalculator.plus(value.getValue(), delta), true);
    }

    @Override
    protected SectionItemValue<T> removePoint(SectionItemValue<T> itemValue, ItemValueType type) {
        if (itemValue == null) {
            return null;
        }
        itemValue.setInclude(false);
        return discretizeValue(itemValue, type);
    }
}
