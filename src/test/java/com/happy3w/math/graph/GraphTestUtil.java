package com.happy3w.math.graph;

import java.util.concurrent.atomic.AtomicLong;

public class GraphTestUtil {
    public static DirectGraph<String, String, Long, Long> createCallGraph(String[] callExps) {
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
