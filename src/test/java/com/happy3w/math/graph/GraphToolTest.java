package com.happy3w.math.graph;

import com.happy3w.java.ext.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class GraphToolTest {

    @Test
    void should_build_graph_success() {
        List<Pair<String, String>> relations = Arrays.asList(
                new Pair<>("A", "B"),
                new Pair<>("A", "C"),
                new Pair<>("B", "C")
        );

        DirectGraph<String, String, String, String> graph = GraphTool.buildGraph(Arrays.asList("A"),
                ids -> loadNodes(ids, relations));

        Assertions.assertEquals(3, graph.nodeCount());
        Assertions.assertEquals(2, graph.node("A").getOutcomes().size());
    }

    private List<GraphNode<String, String, String, String>> loadNodes(List<String> ids, List<Pair<String, String>> relations) {
        List<GraphNode<String, String, String, String>> nodes = new ArrayList<>();
        for (String id : ids) {
            GraphNode<String, String, String, String> node = new GraphNode<>(id);
            for (Pair<String, String> relation : relations) {
                if (relation.getKey().equals(id)) {
                    String relationId = id + relation.getValue();
                    node.withOutcome(new GraphEdge<>(relationId, id, relation.getValue(), relationId));
                }
            }
            nodes.add(node);
        }
        return nodes;
    }

}