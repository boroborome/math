package com.happy3w.math.combination;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

public class GroupCombinationMakerTest {

    @Test
    public void should_make_success_when_normal() {
        GroupCombinationMaker<String> maker = new GroupCombinationMaker<>(new String[]{"0", "1", "2", "3", "4"}, String::equals);
        String finalResult = maker.makeByItemCounts(new int[]{2, 2, 1})
                .map(result -> Arrays.asList(result).stream()
                        .map(it -> Arrays.asList(it))
                        .collect(Collectors.toList())
                )
                .map(it -> it.toString())
                .collect(Collectors.joining("\n"));
        Assertions.assertEquals(
                "[[0, 1], [2, 3], [4]]\n" +
                        "[[0, 1], [2, 4], [3]]\n" +
                        "[[0, 1], [3, 4], [2]]\n" +
                        "[[0, 2], [1, 3], [4]]\n" +
                        "[[0, 2], [1, 4], [3]]\n" +
                        "[[0, 2], [3, 4], [1]]\n" +
                        "[[0, 3], [1, 4], [2]]\n" +
                        "[[0, 3], [2, 4], [1]]\n" +
                        "[[1, 2], [0, 3], [4]]\n" +
                        "[[1, 2], [0, 4], [3]]\n" +
                        "[[1, 2], [3, 4], [0]]\n" +
                        "[[1, 3], [0, 4], [2]]\n" +
                        "[[1, 3], [2, 4], [0]]\n" +
                        "[[2, 3], [0, 4], [1]]\n" +
                        "[[2, 3], [1, 4], [0]]",
                finalResult);
    }

    @Test
    public void should_make_success_when_all_same() {
        GroupCombinationMaker<String> maker = new GroupCombinationMaker<>(new String[]{"1", "1", "1", "1", "1"}, String::equals);
        String finalResult = maker.makeByItemCounts(new int[]{2, 2, 1})
                .map(result -> Arrays.asList(result).stream()
                        .map(it -> Arrays.asList(it))
                        .collect(Collectors.toList())
                )
                .map(it -> it.toString())
                .collect(Collectors.joining("\n"));
        Assertions.assertEquals(
                "[[1, 1], [1, 1], [1]]",
                finalResult);
    }

    @Test
    public void should_make_success_when_mix() {
        GroupCombinationMaker<String> maker = new GroupCombinationMaker<>(new String[]{"1", "2", "1", "2", "1"}, String::equals);
        String finalResult = maker.makeByItemCounts(new int[]{2, 2, 1})
                .map(result -> Arrays.asList(result).stream()
                        .map(it -> Arrays.asList(it))
                        .collect(Collectors.toList())
                )
                .map(it -> it.toString())
                .collect(Collectors.joining("\n"));
        Assertions.assertEquals(
                "[[1, 1], [1, 2], [2]]\n" +
                        "[[1, 1], [2, 2], [1]]\n" +
                        "[[1, 2], [1, 2], [1]]",
                finalResult);
    }
}
