package com.happy3w.math.section;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SectionItemValue<T> {
    private T value;
    private boolean include;
    private ItemValueType type;

    public SectionItemValue(T value, boolean include, ItemValueType type) {
        this.value = value;
        this.include = value == null ? false : include;
        this.type = type;
    }
}
