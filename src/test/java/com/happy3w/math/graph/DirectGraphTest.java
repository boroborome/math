package com.happy3w.math.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class DirectGraphTest {

    @Test
    void should_enum_success_with_tree() {
        DirectGraph<String, String, Long, Long> callGraph = GraphTestUtil.createCallGraph(new String[]{"M1->M2", "M1->M3", "M2->M4"});

        DirectGraph<Long, ScNode<String, String, Long, Long>, Long, Long> scGraph = callGraph.createScGraph();
        List<List<String>> formatedResult = formatResult(scGraph);

        Assertions.assertEquals(
                Arrays.asList(
                        Arrays.asList("M4"),
                        Arrays.asList("M2"),
                        Arrays.asList("M3"),
                        Arrays.asList("M1")
                ),
                formatedResult);
    }

    private List<List<String>> formatResult(DirectGraph<Long, ScNode<String, String, Long, Long>, Long, Long> scGraph) {
        List<List<String>> batchList = scGraph.nodeStream()
                .map(node -> node.getValue().nodeStream()
                        .map(scNode -> scNode.getId())
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
        return batchList;
    }

    @Test
    void should_enum_success_with_small_circle() {
        DirectGraph<String, String, Long, Long> callGraph = GraphTestUtil.createCallGraph(new String[]{"M1->M2", "M1->M3", "M2->M4", "M4->M2"});

        DirectGraph<Long, ScNode<String, String, Long, Long>, Long, Long> scGraph = callGraph.createScGraph();
        List<List<String>> formatedResult = formatResult(scGraph);

        Assertions.assertEquals(
                Arrays.asList(
                        Arrays.asList("M2", "M4"),
                        Arrays.asList("M3"),
                        Arrays.asList("M1")
                ),
                formatedResult);
    }
}