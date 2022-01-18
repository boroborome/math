package com.happy3w.math.section;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.function.Function;

import static com.happy3w.math.section.ItemValueType.from;
import static com.happy3w.math.section.ItemValueType.to;

public class SectionItemValueComparatorTest {

    @Test
    public void should_compare_success() {
        SectionItemValueComparator<Integer> comparator = new SectionItemValueComparator<Integer>(Comparator.comparing(Function.identity()));

        Assertions.assertTrue(comparator.compare(
                new SectionItemValue<Integer>(null, true), to,
                new SectionItemValue<Integer>(2, false), to
        ) > 0);

        Assertions.assertTrue(comparator.compare(
                new SectionItemValue<Integer>(null, true), to,
                new SectionItemValue<Integer>(2, false), from
        ) > 0);

        Assertions.assertTrue(comparator.compare(
                new SectionItemValue<Integer>(2, true), to,
                new SectionItemValue<Integer>(2, false), from
        ) == 0);

        Assertions.assertTrue(comparator.compare(
                new SectionItemValue<Integer>(2, false), to,
                new SectionItemValue<Integer>(2, true), from
        ) == 0);

        Assertions.assertTrue(comparator.compare(
                new SectionItemValue<Integer>(2, false), from,
                new SectionItemValue<Integer>(2, true), from
        ) == 0);

        Assertions.assertTrue(comparator.compare(
                new SectionItemValue<Integer>(2, true), from,
                new SectionItemValue<Integer>(2, true), from
        ) == 0);

        Assertions.assertTrue(comparator.compare(
                new SectionItemValue<Integer>(2, true), from,
                new SectionItemValue<Integer>(2, false), from
        ) == 0);

        Assertions.assertTrue(comparator.compare(
                new SectionItemValue<Integer>(1, false), from,
                new SectionItemValue<Integer>(2, false), from
        ) < 0);

        Assertions.assertTrue(comparator.compare(
                new SectionItemValue<Integer>(1, false), from,
                new SectionItemValue<Integer>(1, false), from
        ) == 0);

        Assertions.assertTrue(comparator.compare(
                new SectionItemValue<Integer>(1, false), from,
                new SectionItemValue<Integer>(null, false), from
        ) > 0);

        Assertions.assertTrue(comparator.compare(
                new SectionItemValue<Integer>(null, false), from,
                new SectionItemValue<Integer>(null, false), from
        ) == 0);

        Assertions.assertTrue(comparator.compare(
                new SectionItemValue<Integer>(null, false), from,
                new SectionItemValue<Integer>(null, false), to
        ) < 0);

        Assertions.assertTrue(comparator.compare(
                new SectionItemValue<Integer>(null, false), to,
                new SectionItemValue<Integer>(null, false), to
        ) == 0);
    }
}
