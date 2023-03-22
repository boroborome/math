package com.happy3w.math.section;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

class NumDiscreteSectionTest {

    @Test
    void should_instance_success() {
        Assertions.assertEquals(
                Arrays.asList(new NumDiscreteSectionItem(1, 2)),
                new NumDiscreteSection(Arrays.asList(
                        new NumDiscreteSectionItem(1, 2)
                )).items().collect(Collectors.toList())
        );

        Assertions.assertEquals(
                Arrays.asList(new NumDiscreteSectionItem(1, 3)),
                new NumDiscreteSection(Arrays.asList(
                        new NumDiscreteSectionItem(1, 2),
                        new NumDiscreteSectionItem(2, 3)
                )).items().collect(Collectors.toList())
        );

        Assertions.assertEquals(
                Arrays.asList(new NumDiscreteSectionItem(1, 4)),
                new NumDiscreteSection(Arrays.asList(
                        new NumDiscreteSectionItem(1, 2),
                        new NumDiscreteSectionItem(3, 4)
                )).items().collect(Collectors.toList())
        );


        Assertions.assertEquals(
                Arrays.asList(new NumDiscreteSectionItem(1, 5)),
                new NumDiscreteSection(Arrays.asList(
                        new NumDiscreteSectionItem(1, 2),
                        new NumDiscreteSectionItem(4, 5),
                        new NumDiscreteSectionItem(3, 3)
                )).items().collect(Collectors.toList())
        );

        Assertions.assertEquals(
                Arrays.asList(new NumDiscreteSectionItem(1, 4)),
                new NumDiscreteSection(Arrays.asList(
                        new NumDiscreteSectionItem(2, 3),
                        new NumDiscreteSectionItem(1, 4)
                )).items().collect(Collectors.toList())
        );

        Assertions.assertEquals(
                Arrays.asList(new NumDiscreteSectionItem(1, 4)),
                new NumDiscreteSection(Arrays.asList(
                        new NumDiscreteSectionItem(1, 4),
                        new NumDiscreteSectionItem(2, 3)
                )).items().collect(Collectors.toList())
        );


        Assertions.assertEquals(
                Arrays.asList(
                        new NumDiscreteSectionItem(1, 2),
                        new NumDiscreteSectionItem(4L, null)),
                new NumDiscreteSection(Arrays.asList(
                        new NumDiscreteSectionItem(1, 2),
                        new NumDiscreteSectionItem(4L, null)
                )).items().collect(Collectors.toList())
        );

        Assertions.assertEquals(
                Arrays.asList(new NumDiscreteSectionItem(1L, null)),
                new NumDiscreteSection(Arrays.asList(
                        new NumDiscreteSectionItem(1, 2),
                        new NumDiscreteSectionItem(4, 5),
                        new NumDiscreteSectionItem(3L, null)
                )).items().collect(Collectors.toList())
        );

        Assertions.assertEquals(
                Arrays.asList(new NumDiscreteSectionItem(null, 5L)),
                new NumDiscreteSection(Arrays.asList(
                        new NumDiscreteSectionItem(null, 2L),
                        new NumDiscreteSectionItem(4, 5),
                        new NumDiscreteSectionItem(3, 3)
                )).items().collect(Collectors.toList())
        );
    }
}