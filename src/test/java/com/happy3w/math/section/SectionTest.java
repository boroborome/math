package com.happy3w.math.section;

import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.function.Function;

public class SectionTest {

    @Test
    public void test() {
        Assert.assertEquals("(*,*)", new Section<Integer>(Comparator.comparing(Function.identity()), new SectionItem<>(null, null))
                .unionItem(new SectionItem<>(null, 2))
                .toString());
    }

    @Test
    public void should_union_item_success() {
        Assert.assertEquals("[2,5][7,9]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                .unionItem(new SectionItem<>(7, 9))
                .toString());
        Assert.assertEquals("[2,9]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                .unionItem(new SectionItem<>(5, false, 9, true))
                .toString());
        Assert.assertEquals("[0,1][2,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                .unionItem(new SectionItem<>(0, 1))
                .toString());
        Assert.assertEquals("[0,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                .unionItem(new SectionItem<>(0, 2))
                .toString());
        Assert.assertEquals("[0,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                .unionItem(new SectionItem<>(0, 3))
                .toString());

        Assert.assertEquals("[0,2][5,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(5, 5))
                .unionItem(new SectionItem<>(0, 2))
                .toString());
        Assert.assertEquals("[0,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(5, 5))
                .unionItem(new SectionItem<>(0, 5))
                .toString());

        Assert.assertEquals("[0,*)", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, null))
                .unionItem(new SectionItem<>(0, 2))
                .toString());
        Assert.assertEquals("(*,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                .unionItem(new SectionItem<>(null, 2))
                .toString());
        Assert.assertEquals("(*,*)", new Section<Integer>(Comparator.comparing(Function.identity()), new SectionItem<>(null, null))
                .unionItem(new SectionItem<>(null, 2))
                .toString());

        Assert.assertEquals("(*,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, false, 5, true))
                .unionItem(new SectionItem<>(null, 2))
                .toString());
        Assert.assertEquals("(*,2)(2,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, false, 5, true))
                .unionItem(new SectionItem<>(null, false, 2, false))
                .toString());
    }

    @Test
    public void should_sub_success() {
        Assert.assertEquals("[2,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                .subtract(new SectionItem<>(7, 9))
                .toString());
        Assert.assertEquals("[2,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                .subtract(new SectionItem<>(0, 1))
                .toString());
        Assert.assertEquals("(3,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                .subtract(new SectionItem<>(0, 3))
                .toString());
        Assert.assertEquals("[2,2](3,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                .subtract(new SectionItem<>(2, false, 3, true))
                .toString());
        Assert.assertEquals("(2,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                .subtract(new SectionItem<>(0, 2))
                .toString());
        Assert.assertEquals("[2,4)", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                .subtract(new SectionItem<>(4, 5))
                .toString());
        Assert.assertEquals("[2,4)", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                .subtract(new SectionItem<>(4, 6))
                .toString());
        Assert.assertEquals("[2,3)(4,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                .subtract(new SectionItem<>(3, 4))
                .toString());
        Assert.assertEquals("(8,9]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                        .unionItem(new SectionItem<>(8, 9))
                .subtract(new SectionItem<>(null, 8))
                .toString());
    }
}
