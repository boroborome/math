package com.happy3w.math.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

class ScIteratorTest {

    @Test
    void should_enum_success_with_tree() {
        DirectGraph<String, String, Long, Long> callGraph = createCallGraph(new String[]{"M1->M2", "M1->M3", "M2->M4"});
        List<List<String>> scNodes = callGraph.scNodeStream()
                .map(n -> n.idStream().collect(Collectors.toList()))
                .collect(Collectors.toList());

        Assertions.assertEquals(
                Arrays.asList(
                        Arrays.asList("M4"),
                        Arrays.asList("M2"),
                        Arrays.asList("M3"),
                        Arrays.asList("M1")
                ),
                scNodes);
    }

    @Test
    void should_enum_success_with_single_call() {
        DirectGraph<String, String, Long, Long> callGraph = createCallGraph(new String[]{"M1->M2"});
        List<List<String>> scNodes = callGraph.scNodeStream()
                .map(n -> n.idStream().collect(Collectors.toList()))
                .collect(Collectors.toList());

        Assertions.assertEquals(
                Arrays.asList(
                        Arrays.asList("M2"),
                        Arrays.asList("M1")
                ),
                scNodes);
    }

    @Test
    void should_enum_success_with_small_circle() {
        DirectGraph<String, String, Long, Long> callGraph = createCallGraph(new String[]{"M1->M2", "M1->M3", "M2->M4", "M4->M2"});
        List<List<String>> scNodes = callGraph.scNodeStream()
                .map(n -> n.idStream().collect(Collectors.toList()))
                .collect(Collectors.toList());

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
        DirectGraph<String, String, Long, Long> callGraph = createCallGraph(new String[]{"M1->M2", "M1->M3", "M2->M4", "M4->M1"});
        List<List<String>> scNodes = callGraph.scNodeStream()
                .map(n -> n.idStream().collect(Collectors.toList()))
                .collect(Collectors.toList());

        Assertions.assertEquals(
                Arrays.asList(
                        Arrays.asList("M3"),
                        Arrays.asList("M1", "M2", "M4")
                ),
                scNodes);
    }

    @Test
    void should_enum_success_with_double_circle() {
        DirectGraph<String, String, Long, Long> callGraph = createCallGraph(new String[]{"M1->M2", "M1->M3", "M2->M4", "M4->M2", "M3->M4"});
        List<List<String>> scNodes = callGraph.scNodeStream()
                .map(n -> n.idStream().collect(Collectors.toList()))
                .collect(Collectors.toList());

        Assertions.assertEquals(
                Arrays.asList(
                        Arrays.asList("M2", "M3", "M4"),
                        Arrays.asList("M1")
                ),
                scNodes);
    }

    @Test
    void should_enum_success_with_self_circle() {
        DirectGraph<String, String, Long, Long> callGraph = createCallGraph(new String[]{"M1->M2", "M1->M3", "M2->M2"});
        List<List<String>> scNodes = callGraph.scNodeStream()
                .map(n -> n.idStream().collect(Collectors.toList()))
                .collect(Collectors.toList());

        Assertions.assertEquals(
                Arrays.asList(
                        Arrays.asList("M2"),
                        Arrays.asList("M3"),
                        Arrays.asList("M1")
                ),
                scNodes);
    }

    private DirectGraph<String, String, Long, Long> createCallGraph(String[] callExps) {
        AtomicLong idHolder = new AtomicLong(0L);
        DirectGraph<String, String, Long, Long> callGraph = new DirectGraph<>();
        for (String exp : callExps) {
            String[] methods = exp.split("->");

            GraphNode<String, String, Long, Long> fromNode = callGraph.takeNode(methods[0]);
            GraphNode<String, String, Long, Long> toNode = callGraph.takeNode(methods[1]);
            callGraph.acceptEdge(new GraphEdge<>(idHolder.incrementAndGet(), fromNode.getId(), toNode.getId(), 0L));
        }

        return callGraph;
    }
}