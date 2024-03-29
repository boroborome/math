package com.happy3w.math.section;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

class NumRangeTest {

    @Test
    void should_ignore_wrong_item() {
        NumRange range = new NumRange();
        range.unionItem(new NumRangeItem(2, 5))
                        .unionItem(new NumRangeItem(7, 1));

        Assertions.assertEquals("[2,5]",
                range.toString());
    }

    @Test
    void should_parse_from_string_success() {
        Assertions.assertEquals(new NumRange(Arrays.asList(
                        new NumRangeItem(1, 1)
                )), NumRange.parse("[1,1]"));

        Assertions.assertEquals(new NumRange(Arrays.asList(
                        new NumRangeItem(1, 1),
                        new NumRangeItem(5, 7)
                )), NumRange.parse("[1,1];[5,7]"));

        Assertions.assertEquals(new NumRange(Arrays.asList(
                        new NumRangeItem(1, 1),
                        new NumRangeItem(5, 7)
                )), NumRange.parse("[1,1];[5,7]"));

        Assertions.assertEquals(new NumRange(Arrays.asList(
                        new NumRangeItem(1L, null)
                )), NumRange.parse("[1,*];[5,7]"));

        Assertions.assertEquals(new NumRange(Arrays.asList(
                new NumRangeItem(null, null)
        )), NumRange.parse("[1,*];[*,7]"));

        Assertions.assertEquals(new NumRange(Arrays.asList(
                new NumRangeItem(null, 4L)
        )), NumRange.parse("[1,2];[*,4]"));

        Assertions.assertEquals(new NumRange(Arrays.asList(
                new NumRangeItem(null, 4L)
        )), NumRange.parse("[*,4];[1,2]"));
    }

    @Test
    void should_to_string_success() {
        Assertions.assertEquals("",
                new NumRange().toString());

        Assertions.assertEquals("[1,1]",
                new NumRange(Arrays.asList(
                        new NumRangeItem(1, 1)
                )).toString());

        Assertions.assertEquals("[1,1];[5,8]",
                new NumRange(Arrays.asList(
                        new NumRangeItem(1, 1),
                        new NumRangeItem(5, 8)
                )).toString());

        Assertions.assertEquals("(*,1];[5,*)",
                new NumRange(Arrays.asList(
                        new NumRangeItem(null, 1L),
                        new NumRangeItem(5L, null)
                )).toString());
    }

    @Test
    void should_calculate_size_success() {
        Assertions.assertEquals(0, new NumRange().size());

        Assertions.assertEquals(1, new NumRange(Arrays.asList(
                new NumRangeItem(1, 1))
        ).size());

        Assertions.assertEquals(null, new NumRange(Arrays.asList(
                new NumRangeItem(1L, null))
        ).size());

        Assertions.assertEquals(7, new NumRange(Arrays.asList(
                new NumRangeItem(1, 3),
                new NumRangeItem(6, 9)
        )
        ).size());
    }

    @Test
    void should_subtract_success() {
        Assertions.assertEquals(
                Arrays.asList(new NumRangeItem(1, 3),
                        new NumRangeItem(7, 7)),
                new NumRange(Arrays.asList(
                        new NumRangeItem(1, 3),
                        new NumRangeItem(5, 7)
                )).subtractItem(new NumRangeItem(4, 6))
                        .items().collect(Collectors.toList())
        );

        Assertions.assertEquals(
                Arrays.asList(new NumRangeItem(null, 4L),
                        new NumRangeItem(8L, null)),
                new NumRange(Arrays.asList(
                        new NumRangeItem(null, null)
                )).subtractItem(new NumRangeItem(5, 7))
                        .items().collect(Collectors.toList())
        );

        Assertions.assertEquals(
                Arrays.asList(new NumRangeItem(1, 4),
                        new NumRangeItem(8, 10)),
                new NumRange(Arrays.asList(
                        new NumRangeItem(1, 10)
                )).subtractItem(new NumRangeItem(5, 7))
                        .items().collect(Collectors.toList())
        );

        Assertions.assertEquals(
                Arrays.asList(new NumRangeItem(1, 4),
                        new NumRangeItem(8, 10)),
                new NumRange(Arrays.asList(
                        new NumRangeItem(1, 5),
                        new NumRangeItem(7, 10)
                )).subtractItem(new NumRangeItem(5, 7))
                        .items().collect(Collectors.toList())
        );

        Assertions.assertEquals(
                Arrays.asList(new NumRangeItem(1, 1),
                        new NumRangeItem(12, 15)),
                new NumRange(Arrays.asList(
                        new NumRangeItem(1, 3),
                        new NumRangeItem(5, 7),
                        new NumRangeItem(10, 15)
                )).subtractItem(new NumRangeItem(2, 11))
                        .items().collect(Collectors.toList())
        );
    }

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