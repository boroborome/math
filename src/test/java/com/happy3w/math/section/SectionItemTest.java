package com.happy3w.math.section;

import org.junit.Assert;
import org.junit.Test;

public class SectionItemTest {

    @Test
    public void testToString() {
        Assert.assertEquals("[2,5]", new SectionItem<>(2, true, 5, true).toString());
        Assert.assertEquals("[2,5)", new SectionItem<>(2, true, 5, false).toString());
        Assert.assertEquals("(2,5)", new SectionItem<>(2, false, 5, false).toString());
        Assert.assertEquals("(2,5]", new SectionItem<>(2, false, 5, true).toString());
        Assert.assertEquals("[2,*)", new SectionItem<>(2, null).toString());
        Assert.assertEquals("(*,5]", new SectionItem<>(null, 5).toString());
        Assert.assertEquals("(*,5]", new SectionItem<>(null, true, 5, true).toString());
        Assert.assertEquals("(*,*)", new SectionItem<>(null, null).toString());
    }
}
