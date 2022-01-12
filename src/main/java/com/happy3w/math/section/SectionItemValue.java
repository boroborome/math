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

    public SectionItemValue(T value, boolean include) {
        this.value = value;
        this.include = value == null ? false : include;
    }

    @Override
    public String toString() {
        return value + "," + include;
    }
}
