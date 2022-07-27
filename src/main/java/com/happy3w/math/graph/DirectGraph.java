package com.happy3w.math.graph;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Getter
public class DirectGraph<NK, NV, EK, EV> {
    private Map<NK, GraphNode<NK, NV, EK, EV>> nodes = new HashMap<>();

    public GraphNode<NK, NV, EK, EV> takeNode(NK id, NV value) {
        GraphNode<NK, NV, EK, EV> node = nodes.computeIfAbsent(id, key -> new GraphNode<>(id));
        node.setValue(value);
        return node;
    }

    public GraphNode<NK, NV, EK, EV> takeNode(NK id) {
        return nodes.computeIfAbsent(id, key -> new GraphNode<>(id));
    }

    public Stream<GraphNode<NK, NV, EK, EV>> nodeStream() {
        return nodes.values().stream();
    }

    public Stream<NV> nodeValueStream() {
        return nodes.values().stream()
                .map(GraphNode::getValue);
    }

    public NV nodeValue(NK nodeId) {
        GraphNode<NK, NV, EK, EV> node = nodes.get(nodeId);
        return node == null ? null : node.getValue();
    }

    public Stream<GraphEdge<EK, EV, NK>> outcomes(NK id) {
        GraphNode<NK, NV, EK, EV> node = nodes.get(id);
        if (node == null) {
            return Stream.empty();
        }
        return node.outcomeStream();
    }

    public void acceptEdge(GraphEdge<EK, EV, NK> edge) {
        takeNode(edge.getFrom())
                .withOutcome(edge);
        takeNode(edge.getTo())
                .withIncome(edge);
    }

    public void acceptNodes(List<GraphNode<NK, NV, EK, EV>> newNodes) {
        for (GraphNode<NK, NV, EK, EV> node : newNodes) {
            nodes.put(node.getId(), node);
        }
    }

    public int nodeCount() {
        return nodes.size();
    }

    public GraphNode<NK, NV, EK, EV> node(NK nodeId) {
        return nodes.get(nodeId);
    }

    public void initIncomeWithOutcome() {
        nodeStream()
                .flatMap(GraphNode::outcomeStream)
                .forEach(outcome -> node(outcome.getTo())
                        .withIncome(outcome));
    }

    public void initOutcomeWithIncome() {
        nodeStream()
                .flatMap(GraphNode::incomeStream)
                .forEach(income -> node(income.getFrom())
                        .withOutcome(income));
    }

    public DirectGraph<NK, NV, EK, EV> filterNodes(Predicate<GraphNode<NK, NV, EK, EV>> nodeChecker) {
        for (GraphNode<NK, NV, EK, EV> node : new ArrayList<>(nodes.values())) {
            if (!nodeChecker.test(node)) {
                cleanRelation(node);
                nodes.remove(node.getId());
            }
        }
        return this;
    }

    private void cleanRelation(GraphNode<NK, NV, EK, EV> node) {
        node.incomeStream()
                .forEach(income -> node(income.getFrom()).removeOutcome(income));
        node.outcomeStream()
                .forEach(outcome -> node(outcome.getTo()).removeIncome(outcome));
    }
}
