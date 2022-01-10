package com.happy3w.math.section;

import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.function.Function;

public class SectionTest {

    @Test
    public void should_merge_success() {
        Assert.assertEquals("[2,5][7,9]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                .merge(new SectionItem<>(7, 9))
                .toString());
        Assert.assertEquals("[0,1][2,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                .merge(new SectionItem<>(0, 1))
                .toString());
        Assert.assertEquals("[0,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                .merge(new SectionItem<>(0, 2))
                .toString());
        Assert.assertEquals("[0,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                .merge(new SectionItem<>(0, 3))
                .toString());

        Assert.assertEquals("[0,2][5,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(5, 5))
                .merge(new SectionItem<>(0, 2))
                .toString());
        Assert.assertEquals("[0,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(5, 5))
                .merge(new SectionItem<>(0, 5))
                .toString());

        Assert.assertEquals("[0,*)", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, null))
                .merge(new SectionItem<>(0, 2))
                .toString());
        Assert.assertEquals("(*,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, 5))
                .merge(new SectionItem<>(null, 2))
                .toString());
        Assert.assertEquals("(*,*)", new Section<Integer>(Comparator.comparing(Function.identity()), new SectionItem<>(null, null))
                .merge(new SectionItem<>(null, 2))
                .toString());

        Assert.assertEquals("(*,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, false, 5, true))
                .merge(new SectionItem<>(null, 2))
                .toString());
        Assert.assertEquals("(*,2)(2,5]", new Section<>(Comparator.comparing(Function.identity()), new SectionItem<>(2, false, 5, true))
                .merge(new SectionItem<>(null, false, 2, false))
                .toString());
    }
}
