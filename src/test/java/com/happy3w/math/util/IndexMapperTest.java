package com.happy3w.math.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IndexMapperTest {

    @Test
    public void should_init_success() {
        IndexMapper<String> mapper = new IndexMapper<>(new String[]{"1", "2", "3", "1"}, String::equals);
        Assertions.assertArrayEquals(new int[]{2, 1, 1}, mapper.getMetaCounts());
        Assertions.assertArrayEquals(new int[]{0, 0, 1, 2}, mapper.createStartValues());
        Assertions.assertArrayEquals(new String[]{"2", "1", "3", "1"}, mapper.convertValues(new int[]{1, 0, 2, 0}));
    }
}
