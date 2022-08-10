package com.happy3w.math.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ScIteratorTest {

    @Test
    void should_enum_success_with_tree() {
        DirectGraph<String, String, Long, Long> callGraph = GraphTestUtil.createCallGraph(new String[]{"M1->M2", "M1->M3", "M2->M4"});

        Stream<ScNode<String, String, Long, Long>> result = callGraph.scNodeStream();
        List<List<String>> scNodes = formatResult(result);

        Assertions.assertEquals(
                Arrays.asList(
                        Arrays.asList("M4"),
                        Arrays.asList("M2"),
                        Arrays.asList("M3"),
                        Arrays.asList("M1")
                ),
                scNodes);
    }

    private List<List<String>> formatResult(Stream<ScNode<String, String, Long, Long>> result) {
        List<List<String>> scNodes = result
                .map(n -> n.idStream().collect(Collectors.toList()))
                .collect(Collectors.toList());
        return scNodes;
    }

    @Test
    void should_enum_success_with_single_call() {
        DirectGraph<String, String, Long, Long> callGraph = GraphTestUtil.createCallGraph(new String[]{"M1->M2"});

        Stream<ScNode<String, String, Long, Long>> result = callGraph.scNodeStream();
        List<List<String>> scNodes = formatResult(result);

        Assertions.assertEquals(
                Arrays.asList(
                        Arrays.asList("M2"),
                        Arrays.asList("M1")
                ),
                scNodes);
    }

    @Test
    void should_enum_success_with_small_circle() {
        DirectGraph<String, String, Long, Long> callGraph = GraphTestUtil.createCallGraph(new String[]{"M1->M2", "M1->M3", "M2->M4", "M4->M2"});

        Stream<ScNode<String, String, Long, Long>> result = callGraph.scNodeStream();
        List<List<String>> scNodes = formatResult(result);

        Assertions.assertEquals(
                Arrays.asList(
                        Arrays.asList("M2", "M4"),
                        Arrays.asList("M3"),
                        Arrays.asList("M1")
                ),
                scNodes);
    }

    @Test
    void should_enum_success_with_big_circle() {
        DirectGraph<String, String, Long, Long> callGraph = GraphTestUtil.createCallGraph(new String[]{"M1->M2", "M1->M3", "M2->M4", "M4->M1"});

        Stream<ScNode<String, String, Long, Long>> result = callGraph.scNodeStream();
        List<List<String>> scNodes = formatResult(result);

        Assertions.assertEquals(
                Arrays.asList(
                        Arrays.asList("M3"),
                        Arrays.asList("M1", "M2", "M4")
                ),
                scNodes);
    }

    @Test
    void should_enum_success_with_double_circle() {
        DirectGraph<String, String, Long, Long> callGraph = GraphTestUtil.createCallGraph(new String[]{"M1->M2", "M1->M3", "M2->M4", "M4->M2", "M3->M4", "M2->M3"});

        Stream<ScNode<String, String, Long, Long>> result = callGraph.scNodeStream();
        List<List<String>> scNodes = formatResult(result);

        Assertions.assertEquals(
                Arrays.asList(
                        Arrays.asList("M2", "M3", "M4"),
                        Arrays.asList("M1")
                ),
                scNodes);
    }

    @Test
    void should_enum_success_with_self_circle() {
        DirectGraph<String, String, Long, Long> callGraph = GraphTestUtil.createCallGraph(new String[]{"M1->M2", "M1->M3", "M2->M2"});

        Stream<ScNode<String, String, Long, Long>> result = callGraph.scNodeStream();
        List<List<String>> scNodes = formatResult(result);

        Assertions.assertEquals(
                Arrays.asList(
                        Arrays.asList("M2"),
                        Arrays.asList("M3"),
                        Arrays.asList("M1")
                ),
                scNodes);
    }

}