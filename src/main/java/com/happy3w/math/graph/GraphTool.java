package com.happy3w.math.graph;

import com.happy3w.math.tree.SimpleTreeNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;
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

    /**
     * Convert graph to tree
     * @param graph graph to convert
     * @param startNodeId the start tree node
     * @param treeNodeGenerator generator to create tree node, the boolean param is repeated.
     * @return tree created from graph
     * @param <NK> graph node key type
     * @param <NV> graph node value type
     * @param <EK> graph edge key type
     * @param <EV> graph edge value type
     * @param <TT> tree data type
     */
    public static <NK, NV, EK, EV, TT> SimpleTreeNode<TT> graphToTree(DirectGraph<NK, NV, EK, EV> graph,
                                                                      NK startNodeId,
                                                                      BiFunction<NV, Boolean, TT> treeNodeGenerator) {
        AtomicReference<SimpleTreeNode<TT>> treeHolder = new AtomicReference<>(null);
        Set<NK> visitedNodes = new HashSet<>();
        Queue<GraphTreeNodeHolder<NK, NV, EK, EV, TT>> queue = new LinkedList<>();

        GraphNode<NK, NV, EK, EV> startStackNode = graph.node(startNodeId);
        queue.add(new GraphTreeNodeHolder<>(startStackNode, treeNode -> treeHolder.set(treeNode)));

        for (GraphTreeNodeHolder<NK, NV, EK, EV, TT> curHolder = queue.poll(); curHolder != null; curHolder = queue.poll()) {
            GraphNode<NK, NV, EK, EV> graphNode = curHolder.getGraphNode();
            boolean repeated = visitedNodes.contains(graphNode.getId());
            TT treeNodeData = treeNodeGenerator.apply(graphNode.getValue(), repeated);
            SimpleTreeNode<TT> curTreeNode = new SimpleTreeNode<>(treeNodeData);
            curHolder.getTreeNodeCollector().accept(curTreeNode);

            if (repeated) {
                continue;
            }

            visitedNodes.add(graphNode.getId());
            graphNode.outcomeStream()
                    .sorted(Comparator.comparing(GraphEdge::getValue, (Comparator<EV>) Comparator.naturalOrder()))
                    .map(edge -> graph.node(edge.getTo()))
                    .forEach(node -> queue.add(new GraphTreeNodeHolder<>(node, treeNode -> curTreeNode.addSubNode(treeNode))));
        }
        return treeHolder.get();
    }

    @Getter
    @AllArgsConstructor
    private static class GraphTreeNodeHolder<NK, NV, EK, EV, TT> {
        private GraphNode<NK, NV, EK, EV> graphNode;
        private Consumer<SimpleTreeNode<TT>> treeNodeCollector;
    }
}
