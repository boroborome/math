package com.happy3w.math.section;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

class NumRangeTest {

    @Test
    void should_instance_success() {
        Assertions.assertEquals(
                Arrays.asList(new NumRangeItem(1, 2)),
                new NumRange(Arrays.asList(
                        new NumRangeItem(1, 2)
                )).items().collect(Collectors.toList())
        );

        Assertions.assertEquals(
                Arrays.asList(new NumRangeItem(1, 3)),
                new NumRange(Arrays.asList(
                        new NumRangeItem(1, 2),
                        new NumRangeItem(2, 3)
                )).items().collect(Collectors.toList())
        );

        Assertions.assertEquals(
                Arrays.asList(new NumRangeItem(1, 4)),
                new NumRange(Arrays.asList(
                        new NumRangeItem(1, 2),
                        new NumRangeItem(3, 4)
                )).items().collect(Collectors.toList())
        );


        Assertions.assertEquals(
                Arrays.asList(new NumRangeItem(1, 5)),
                new NumRange(Arrays.asList(
                        new NumRangeItem(1, 2),
                        new NumRangeItem(4, 5),
                        new NumRangeItem(3, 3)
                )).items().collect(Collectors.toList())
        );

        Assertions.assertEquals(
                Arrays.asList(new NumRangeItem(1, 4)),
                new NumRange(Arrays.asList(
                        new NumRangeItem(2, 3),
                        new NumRangeItem(1, 4)
                )).items().collect(Collectors.toList())
        );

        Assertions.assertEquals(
                Arrays.asList(new NumRangeItem(1, 4)),
                new NumRange(Arrays.asList(
                        new NumRangeItem(1, 4),
                        new NumRangeItem(2, 3)
                )).items().collect(Collectors.toList())
        );


        Assertions.assertEquals(
                Arrays.asList(
                        new NumRangeItem(1, 2),
                        new NumRangeItem(4L, null)),
                new NumRange(Arrays.asList(
                        new NumRangeItem(1, 2),
                        new NumRangeItem(4L, null)
                )).items().collect(Collectors.toList())
        );

        Assertions.assertEquals(
                Arrays.asList(new NumRangeItem(1L, null)),
                new NumRange(Arrays.asList(
                        new NumRangeItem(1, 2),
                        new NumRangeItem(4, 5),
                        new NumRangeItem(3L, null)
                )).items().collect(Collectors.toList())
        );

        Assertions.assertEquals(
                Arrays.asList(new NumRangeItem(null, 5L)),
                new NumRange(Arrays.asList(
                        new NumRangeItem(null, 2L),
                        new NumRangeItem(4, 5),
                        new NumRangeItem(3, 3)
                )).items().collect(Collectors.toList())
        );
    }
}