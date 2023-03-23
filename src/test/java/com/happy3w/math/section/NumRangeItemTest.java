package com.happy3w.math.section;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NumRangeItemTest {

    @Test
    void should_parse_success() {
        Assertions.assertEquals(null, NumRangeItem.parse(""));
        Assertions.assertEquals(null, NumRangeItem.parse("[4,3]"));
        Assertions.assertEquals(new NumRangeItem(3, 4), NumRangeItem.parse("[3,4]"));
        Assertions.assertEquals(new NumRangeItem(3, 3), NumRangeItem.parse("[3,3]"));
        Assertions.assertEquals(new NumRangeItem(3, 3), NumRangeItem.parse("[3]"));
        Assertions.assertEquals(new NumRangeItem(4, 4), NumRangeItem.parse("(3,4]"));
        Assertions.assertEquals(new NumRangeItem(4, 4), NumRangeItem.parse("(3,5)"));
        Assertions.assertEquals(new NumRangeItem(null, 4L), NumRangeItem.parse("(*,5)"));
        Assertions.assertEquals(new NumRangeItem(null, 4L), NumRangeItem.parse("(,5)"));
        Assertions.assertEquals(new NumRangeItem(null, null), NumRangeItem.parse("(,)"));
        Assertions.assertEquals(new NumRangeItem(null, null), NumRangeItem.parse("[,]"));
        Assertions.assertEquals(new NumRangeItem(null, 4L), NumRangeItem.parse("[*,5)"));
        Assertions.assertEquals(new NumRangeItem(null, 5L), NumRangeItem.parse("( * , 5 ]"));
    }
}