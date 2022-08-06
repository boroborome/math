package com.happy3w.math.combination;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class NormalCombineMakerTest {

    @Test
    void should_combine_success_2() {
        List<List<String>> result = new NormalCombineMaker<>(Arrays.asList("A", "B", "C", "D"))
                .combineStream(2)
                .collect(Collectors.toList());
        Assertions.assertEquals(
                Arrays.asList(
                        Arrays.asList("A", "B"),
                        Arrays.asList("A", "C"),
                        Arrays.asList("A", "D"),
                        Arrays.asList("B", "C"),
                        Arrays.asList("B", "D"),
                        Arrays.asList("C", "D")
                ),
                result
        );
    }

    @Test
    void should_combine_success_3() {
        List<List<String>> result = new NormalCombineMaker<>(Arrays.asList("A", "B", "C", "D"))
                .combineStream(3)
                .collect(Collectors.toList());
        Assertions.assertEquals(
                Arrays.asList(
                        Arrays.asList("A", "B", "C"),
                        Arrays.asList("A", "B", "D"),
                        Arrays.asList("A", "C", "D"),
                        Arrays.asList("B", "C", "D")
                ),
                result
        );
    }

}