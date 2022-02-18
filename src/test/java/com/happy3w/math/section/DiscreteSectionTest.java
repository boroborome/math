package com.happy3w.math.section;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

public class DiscreteSectionTest {

    @Test
    public void should_union_item_success() {
        Assertions.assertEquals("[5,9]", new DiscreteSection<>(DiscretizeCalculators.intCalculator)
                .unionItem(SectionItem.ofValue(5, 9))
                .toString());

        Assertions.assertEquals("[2,4][6,9]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, true, 5, false))
                .unionItem(SectionItem.ofValue(5, false, 9, true))
                .toString());
        Assertions.assertEquals("[2,5][7,9]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(7, 9))
                .toString());
        Assertions.assertEquals("[2,9]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(5, false, 9, true))
                .toString());
        Assertions.assertEquals("[0,5]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(0, 1))
                .toString());
        Assertions.assertEquals("[0,5]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(0, 2))
                .toString());
        Assertions.assertEquals("[0,5]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(0, 3))
                .toString());

        Assertions.assertEquals("[0,2][5,5]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(5, 5))
                .unionItem(SectionItem.ofValue(0, 2))
                .toString());
        Assertions.assertEquals("[0,5]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(5, 5))
                .unionItem(SectionItem.ofValue(0, 5))
                .toString());

        Assertions.assertEquals("[0,*)", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, null))
                .unionItem(SectionItem.ofValue(0, 2))
                .toString());
        Assertions.assertEquals("(*,5]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(null, 2))
                .toString());
        Assertions.assertEquals("(*,*)", new DiscreteSection<Integer>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(null, null))
                .unionItem(SectionItem.ofValue(null, 2))
                .toString());

        Assertions.assertEquals("(*,5]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, false, 5, true))
                .unionItem(SectionItem.ofValue(null, 2))
                .toString());
        Assertions.assertEquals("(*,1][3,5]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, false, 5, true))
                .unionItem(SectionItem.ofValue(null, false, 2, false))
                .toString());
        Assertions.assertEquals("[-1,5]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, false, 5, true))
                .unionItem(SectionItem.ofValue(-2, false, 2, false))
                .unionItem(SectionItem.ofValue(0, false, 2, true))
                .toString());
    }

    @Test
    public void should_sub_success() {
        Assertions.assertEquals("", new DiscreteSection<>(DiscretizeCalculators.intCalculator)
                .subtractItem(SectionItem.ofValue(7, 9))
                .toString());

        Assertions.assertEquals("[2,5]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(7, 9))
                .toString());
        Assertions.assertEquals("[2,5]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(0, 1))
                .toString());
        Assertions.assertEquals("[4,5]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(0, 3))
                .toString());
        Assertions.assertEquals("[2,2][4,5]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(2, false, 3, true))
                .toString());
        Assertions.assertEquals("[3,5]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(0, 2))
                .toString());
        Assertions.assertEquals("[2,3]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(4, 5))
                .toString());
        Assertions.assertEquals("[2,3]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(4, 6))
                .toString());
        Assertions.assertEquals("[2,2][5,5]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(3, 4))
                .toString());
        Assertions.assertEquals("[9,9]", new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(8, 9))
                .subtractItem(SectionItem.ofValue(null, 8))
                .toString());
    }

    @Test
    public void should_subtract_section_to_empty_success() {
        DiscreteSection<Integer> section1 = new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(null, 5));
        DiscreteSection<Integer> section2 = new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(null, 6),
                SectionItem.ofValue(8, 12));

        section1.subtractSection(section2);
        Assertions.assertEquals("", section1.toString());
    }


    @Test
    public void should_subtract_section_to_empty_when_same_success() {
        Date start = Timestamp.valueOf("2022-02-16 00:00:00");
        Date end = Timestamp.valueOf("2022-02-18 00:00:00");
        DiscreteSection<Date> result = new DiscreteSection<>(DiscretizeCalculators.dateCalculator, SectionItem.ofValue(start, true, end, true))
                .subtractItem(SectionItem.ofValue(start, true, end, true));
        Assertions.assertEquals("", result.toString());
    }

    @Test
    public void should_subtract_section_with_left_success() {
        DiscreteSection<Integer> section1 = new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(null, 5));
        DiscreteSection<Integer> section2 = new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(3, 6),
                SectionItem.ofValue(8, 12));

        section1.subtractSection(section2);
        Assertions.assertEquals("(*,2]", section1.toString());
    }

    @Test
    public void should_reverse_success() {
        DiscreteSection<Integer> section = new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, 5));
        DiscreteSection<Integer> reverseSection = section.reverse();
        Assertions.assertEquals("(*,1][6,*)", reverseSection.toString());

        DiscreteSection<Integer> reverseSection2 = reverseSection.reverse();
        Assertions.assertEquals("[2,5]", reverseSection2.toString());
    }

    @Test
    public void should_stream_success() {
        DiscreteSection<Integer> section = new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, 5));
        Assertions.assertEquals(Arrays.asList(2, 3, 4, 5),
                section.stream()
                        .collect(Collectors.toList()));

        Assertions.assertEquals(Arrays.asList(5, 4, 3, 2),
                section.streamDesc()
                        .collect(Collectors.toList()));

//        DiscreteSection<Integer> section2 = new DiscreteSection<>(DiscretizeCalculators.intCalculator, SectionItem.ofValue(2, 5), SectionItem.ofValue(8, null));
//        Assertions.assertEquals(Arrays.asList(2, 3, 4, 5, 8, 9),
//                section2.stream()
//                        .limit(6)
//                        .collect(Collectors.toList()));
    }
}
