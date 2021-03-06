package com.happy3w.math.permutation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SimplePermutationMakerTest {

    @Test
    public void should_gen_success() {
        List<List<String>> results = new SimplePermutationMaker<>(new String[]{"1", "2", "3"})
                .generate()
                .map(vs -> Arrays.asList(vs))
                .collect(Collectors.toList());
        Assertions.assertEquals("[[1, 2, 3], [1, 3, 2], [2, 1, 3], [2, 3, 1], [3, 1, 2], [3, 2, 1]]",
                results.toString());
    }
}
