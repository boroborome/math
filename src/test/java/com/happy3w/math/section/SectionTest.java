package com.happy3w.math.section;

import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.function.Function;

public class SectionTest {

    @Test
    public void should_union_item_success() {
        Assert.assertEquals("[2,5)(5,9]", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, true, 5, false))
                .unionItem(SectionItem.ofValue(5, false, 9, true))
                .toString());
        Assert.assertEquals("[2,5][7,9]", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(7, 9))
                .toString());
        Assert.assertEquals("[2,9]", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(5, false, 9, true))
                .toString());
        Assert.assertEquals("[0,1][2,5]", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(0, 1))
                .toString());
        Assert.assertEquals("[0,5]", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(0, 2))
                .toString());
        Assert.assertEquals("[0,5]", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(0, 3))
                .toString());

        Assert.assertEquals("[0,2][5,5]", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(5, 5))
                .unionItem(SectionItem.ofValue(0, 2))
                .toString());
        Assert.assertEquals("[0,5]", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(5, 5))
                .unionItem(SectionItem.ofValue(0, 5))
                .toString());

        Assert.assertEquals("[0,*)", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, null))
                .unionItem(SectionItem.ofValue(0, 2))
                .toString());
        Assert.assertEquals("(*,5]", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .unionItem(SectionItem.ofValue(null, 2))
                .toString());
        Assert.assertEquals("(*,*)", new Section<Integer>(Comparator.comparing(Function.identity()), SectionItem.ofValue(null, null))
                .unionItem(SectionItem.ofValue(null, 2))
                .toString());

        Assert.assertEquals("(*,5]", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, false, 5, true))
                .unionItem(SectionItem.ofValue(null, 2))
                .toString());
        Assert.assertEquals("(*,2)(2,5]", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, false, 5, true))
                .unionItem(SectionItem.ofValue(null, false, 2, false))
                .toString());
        Assert.assertEquals("(-2,5]", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, false, 5, true))
                .unionItem(SectionItem.ofValue(-2, false, 2, false))
                .unionItem(SectionItem.ofValue(0, false, 2, true))
                .toString());
    }

    @Test
    public void should_sub_success() {
        Assert.assertEquals("[2,5]", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .subtract(SectionItem.ofValue(7, 9))
                .toString());
        Assert.assertEquals("[2,5]", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .subtract(SectionItem.ofValue(0, 1))
                .toString());
        Assert.assertEquals("(3,5]", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .subtract(SectionItem.ofValue(0, 3))
                .toString());
        Assert.assertEquals("[2,2](3,5]", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .subtract(SectionItem.ofValue(2, false, 3, true))
                .toString());
        Assert.assertEquals("(2,5]", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .subtract(SectionItem.ofValue(0, 2))
                .toString());
        Assert.assertEquals("[2,4)", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .subtract(SectionItem.ofValue(4, 5))
                .toString());
        Assert.assertEquals("[2,4)", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .subtract(SectionItem.ofValue(4, 6))
                .toString());
        Assert.assertEquals("[2,3)(4,5]", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                .subtract(SectionItem.ofValue(3, 4))
                .toString());
        Assert.assertEquals("(8,9]", new Section<>(Comparator.comparing(Function.identity()), SectionItem.ofValue(2, 5))
                        .unionItem(SectionItem.ofValue(8, 9))
                .subtract(SectionItem.ofValue(null, 8))
                .toString());
    }
}
