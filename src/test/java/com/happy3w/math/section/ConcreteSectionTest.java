package com.happy3w.math.section;

import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.function.Function;

public class ConcreteSectionTest {

    @Test
    public void should_union_item_success() {
        Assert.assertEquals("[5,9]", new ConcreteSection<Integer>(Comparator.comparing(Function.identity()))
                .unionItem(SectionItem.ofValue(5, 9))
                .toString());

        Assert.assertEquals("[2,5)(5,9]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, true, 5, false))
                .unionItem(SectionItem.ofValue(5, false, 9, true))
                .toString());
        Assert.assertEquals("[2,5][7,9]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(7, 9))
                .toString());
        Assert.assertEquals("[2,9]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(5, false, 9, true))
                .toString());
        Assert.assertEquals("[0,1][2,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(0, 1))
                .toString());
        Assert.assertEquals("[0,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(0, 2))
                .toString());
        Assert.assertEquals("[0,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(0, 3))
                .toString());

        Assert.assertEquals("[0,2][5,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(5, 5))
                .unionItem(SectionItem.ofValue(0, 2))
                .toString());
        Assert.assertEquals("[0,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(5, 5))
                .unionItem(SectionItem.ofValue(0, 5))
                .toString());

        Assert.assertEquals("[0,*)", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, null))
                .unionItem(SectionItem.ofValue(0, 2))
                .toString());
        Assert.assertEquals("(*,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(null, 2))
                .toString());
        Assert.assertEquals("(*,*)", new ConcreteSection<Integer>(Comparator.comparing(Function.identity()), SectionItem.ofValue(null, null))
                .unionItem(SectionItem.ofValue(null, 2))
                .toString());

        Assert.assertEquals("(*,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, false, 5, true))
                .unionItem(SectionItem.ofValue(null, 2))
                .toString());
        Assert.assertEquals("(*,2)(2,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, false, 5, true))
                .unionItem(SectionItem.ofValue(null, false, 2, false))
                .toString());
        Assert.assertEquals("(-2,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, false, 5, true))
                .unionItem(SectionItem.ofValue(-2, false, 2, false))
                .unionItem(SectionItem.ofValue(0, false, 2, true))
                .toString());
    }

    @Test
    public void should_sub_success() {
        Assert.assertEquals("", new ConcreteSection<>(Integer.class)
                .subtractItem(SectionItem.ofValue(7, 9))
                .toString());

        Assert.assertEquals("[2,5]", new ConcreteSection<>(Integer.class, SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(7, 9))
                .toString());
        Assert.assertEquals("[2,5]", new ConcreteSection<>(Integer.class, SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(0, 1))
                .toString());
        Assert.assertEquals("(3,5]", new ConcreteSection<>(Integer.class, SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(0, 3))
                .toString());
        Assert.assertEquals("[2,2](3,5]", new ConcreteSection<>(Integer.class, SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(2, false, 3, true))
                .toString());
        Assert.assertEquals("(2,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(0, 2))
                .toString());
        Assert.assertEquals("[2,4)", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(4, 5))
                .toString());
        Assert.assertEquals("[2,4)", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(4, 6))
                .toString());
        Assert.assertEquals("[2,3)(4,5]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .subtractItem(SectionItem.ofValue(3, 4))
                .toString());
        Assert.assertEquals("(8,9]", new ConcreteSection<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                        .unionItem(SectionItem.ofValue(8, 9))
                .subtractItem(SectionItem.ofValue(null, 8))
                .toString());
    }

    @Test
    public void should_contains_success() {
        ConcreteSection<Integer> section2_5 = new ConcreteSection<>(SectionItem.ofValue(2, 5));
        Assert.assertEquals(false, section2_5.contains(1));
        Assert.assertEquals(true, section2_5.contains(2));
        Assert.assertEquals(true, section2_5.contains(3));
        Assert.assertEquals(true, section2_5.contains(5));
        Assert.assertEquals(false, section2_5.contains(6));

        ConcreteSection<Integer> section_not_include = new ConcreteSection<>(SectionItem.ofValue(2, false, 5, false));
        Assert.assertEquals(false, section_not_include.contains(2));
        Assert.assertEquals(false, section_not_include.contains(5));

        ConcreteSection<Integer> open2Section = new ConcreteSection<>(SectionItem.ofValue(null, false, 2, true))
                .unionItem(SectionItem.ofValue(5, true, null, false));
        Assert.assertEquals(true, open2Section.contains(1));
        Assert.assertEquals(true, open2Section.contains(2));
        Assert.assertEquals(false, open2Section.contains(3));
        Assert.assertEquals(true, open2Section.contains(5));
        Assert.assertEquals(true, open2Section.contains(6));

        Assert.assertEquals(true, new ConcreteSection<Integer>(SectionItem.ofValue(null, false, null, false))
                .contains(2));
    }
}
