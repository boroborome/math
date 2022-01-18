package com.happy3w.math.section;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SectionItemTest {

    @Test
    public void testToString() {
        Assertions.assertEquals("[2,5]", SectionItem.ofValue(2, true, 5, true).toString());
        Assertions.assertEquals("[2,5)", SectionItem.ofValue(2, true, 5, false).toString());
        Assertions.assertEquals("(2,5)", SectionItem.ofValue(2, false, 5, false).toString());
        Assertions.assertEquals("(2,5]", SectionItem.ofValue(2, false, 5, true).toString());
        Assertions.assertEquals("[2,*)", SectionItem.ofValue(2, null).toString());
        Assertions.assertEquals("(*,5]", SectionItem.ofValue(null, 5).toString());
        Assertions.assertEquals("(*,5]", SectionItem.ofValue(null, true, 5, true).toString());
        Assertions.assertEquals("(*,*)", SectionItem.ofValue(null, null).toString());
    }
}
