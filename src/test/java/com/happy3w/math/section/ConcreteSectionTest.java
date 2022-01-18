package com.happy3w.math.section;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.function.Function;

public class ConcreteSectionTest {

    @Test
    public void should_union_item_success() {
        Assertions.assertEquals("[5,9]", new ConcreteSection<Integer>(Comparator.comparing(Function.identity()))
                .unionItem(SectionItem.ofValue(5, 9))
                .toString());

        Assertions.assertEquals("[2,5)(5,9]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, true, 5, false))
                .unionItem(SectionItem.ofValue(5, false, 9, true))
                .toString());
        Assertions.assertEquals("[2,5][7,9]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(7, 9))
                .toString());
        Assertions.assertEquals("[2,9]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(5, false, 9, true))
                .toString());
        Assertions.assertEquals("[0,1][2,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(0, 1))
                .toString());
        Assertions.assertEquals("[0,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(0, 2))
                .toString());
        Assertions.assertEquals("[0,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(0, 3))
                .toString());

        Assertions.assertEquals("[0,2][5,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(5, 5))
                .unionItem(SectionItem.ofValue(0, 2))
                .toString());
        Assertions.assertEquals("[0,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(5, 5))
                .unionItem(SectionItem.ofValue(0, 5))
                .toString());

        Assertions.assertEquals("[0,*)", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, null))
                .unionItem(SectionItem.ofValue(0, 2))
                .toString());
        Assertions.assertEquals("(*,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(null, 2))
                .toString());
        Assertions.assertEquals("(*,*)", new ConcreteSection<Integer>(Comparator.comparing(Function.identity()), SectionItem.ofValue(null, null))
                .unionItem(SectionItem.ofValue(null, 2))
                .toString());

        Assertions.assertEquals("(*,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, false, 5, true))
                .unionItem(SectionItem.ofValue(null, 2))
                .toString());
        Assertions.assertEquals("(*,2)(2,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, false, 5, true))
                .unionItem(SectionItem.ofValue(null, false, 2, false))
                .toString());
        Assertions.assertEquals("(-2,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, false, 5, true))
                .unionItem(SectionItem.ofValue(-2, false, 2, false))
                .unionItem(SectionItem.ofValue(0, false, 2, true))
                .toString());
    }

    @Test
    public void should_sub_success() {
        Assertions.assertEquals("(6,8]", new ConcreteSection<>(Integer.class, SectionItem.ofValue(5, 8))
                .subtractItem(SectionItem.ofValue(2, 6))
                .toString());

        Assertions.assertEquals("", new ConcreteSection<>(Integer.class)
                .subtractItem(SectionItem.ofValue(7, 9))
                .toString());

        Assertions.assertEquals("[2,5]", new ConcreteSection<>(Integer.class, SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(7, 9))
                .toString());
        Assertions.assertEquals("[2,5]", new ConcreteSection<>(Integer.class, SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(0, 1))
                .toString());
        Assertions.assertEquals("(3,5]", new ConcreteSection<>(Integer.class, SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(0, 3))
                .toString());
        Assertions.assertEquals("[2,2](3,5]", new ConcreteSection<>(Integer.class, SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(2, false, 3, true))
                .toString());
        Assertions.assertEquals("(2,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(0, 2))
                .toString());
        Assertions.assertEquals("[2,4)", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(4, 5))
                .toString());
        Assertions.assertEquals("[2,4)", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(4, 6))
                .toString());
        Assertions.assertEquals("[2,3)(4,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(3, 4))
                .toString());
        Assertions.assertEquals("(8,9]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                        .unionItem(SectionItem.ofValue(8, 9))
                .subtractItem(SectionItem.ofValue(null, 8))
                .toString());
    }

    @Test
    public void should_contains_success() {
        ConcreteSection<Integer> section2_5 = new ConcreteSection<>(SectionItem.ofValue(2, 5));
        Assertions.assertEquals(false, section2_5.contains(1));
        Assertions.assertEquals(true, section2_5.contains(2));
        Assertions.assertEquals(true, section2_5.contains(3));
        Assertions.assertEquals(true, section2_5.contains(5));
        Assertions.assertEquals(false, section2_5.contains(6));

        ConcreteSection<Integer> section_not_include = new ConcreteSection<>(SectionItem.ofValue(2, false, 5, false));
        Assertions.assertEquals(false, section_not_include.contains(2));
        Assertions.assertEquals(false, section_not_include.contains(5));

        ConcreteSection<Integer> open2Section = new ConcreteSection<>(SectionItem.ofValue(null, false, 2, true))
                .unionItem(SectionItem.ofValue(5, true, null, false));
        Assertions.assertEquals(true, open2Section.contains(1));
        Assertions.assertEquals(true, open2Section.contains(2));
        Assertions.assertEquals(false, open2Section.contains(3));
        Assertions.assertEquals(true, open2Section.contains(5));
        Assertions.assertEquals(true, open2Section.contains(6));

        Assertions.assertEquals(true, new ConcreteSection<Integer>(SectionItem.ofValue(null, false, null, false))
                .contains(2));
    }

    @Test
    public void should_reverse_success() {
        ConcreteSection<Integer> section = new ConcreteSection<>(SectionItem.ofValue(2, 5));
        ConcreteSection<Integer> reverseSection = section.reverse();
        Assertions.assertEquals("(*,2)(5,*)", reverseSection.toString());

        ConcreteSection<Integer> reverseSection2 = reverseSection.reverse();
        Assertions.assertEquals("[2,5]", reverseSection2.toString());
    }

}
