package com.happy3w.math.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GraphTool {
    public static <NK, NV, EK, EV> DirectGraph<NK, NV, EK, EV> buildGraph(List<NK> startNodeIds,
                                                                   Function<List<NK>, List<GraphNode<NK, NV, EK, EV>>> loader) {
        DirectGraph<NK, NV, EK, EV> directGraph = new DirectGraph<>();

        Set<NK> existIds = new HashSet<>();
        for (List<NK> nodesToLoad = new ArrayList<>(startNodeIds);
             !nodesToLoad.isEmpty();) {
            List<GraphNode<NK, NV, EK, EV>> newNodes = loader.apply(nodesToLoad);
            directGraph.acceptNodes(newNodes);

            nodesToLoad = newNodes.stream()
                    .flatMap(node -> node.outcomeStream())
                    .map(GraphEdge::getTo)
                    .filter(to -> !existIds.contains(to))
                    .peek(to -> existIds.add(to))
                    .collect(Collectors.toList());
        }

        return directGraph;
    }
}
