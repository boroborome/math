package com.happy3w.math.graph;

import com.happy3w.java.ext.stream.ParallelSpliterator;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Getter
public class DirectGraph<NK, NV, EK, EV> {
    private Map<NK, GraphNode<NK, NV, EK, EV>> nodes;

    public DirectGraph() {
        nodes = new HashMap<>();
    }

    public DirectGraph(int initialCapacity) {
        nodes = new HashMap<>(initialCapacity);
    }

    public GraphNode<NK, NV, EK, EV> takeNode(NK id, NV value) {
        return takeNode(id, () -> value);
    }

    public GraphNode<NK, NV, EK, EV> takeNode(NK id, Supplier<NV> valueSupplier) {
        return nodes.computeIfAbsent(id, key -> new GraphNode<NK, NV, EK, EV>(id)
                .withValue(valueSupplier.get()));
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

    public void acceptNode(GraphNode<NK, NV, EK, EV> newNode) {
        nodes.put(newNode.getId(), newNode);
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

    public DirectGraph<NK, NV, EK, EV> cutSubGraph(
            Collection<NK> startIds,
            Function<GraphNode<NK, NV, EK, EV>, Stream<NK>> spreadLogic) {
        Set<NK> idsInPath = collectIdInPath(startIds, spreadLogic);
        return filterNodes(item -> idsInPath.contains(item.getId()));
    }

    public Set<NK> collectIdInPath(
            Collection<NK> startIds,
            Function<GraphNode<NK, NV, EK, EV>, Stream<NK>> spreadLogic) {
        Set<NK> idsInPath = new HashSet<>();
        Stack<NK> accNodeIdStack = new Stack<>();
        accNodeIdStack.addAll(startIds);
        while (!accNodeIdStack.isEmpty()) {
            NK nodeId = accNodeIdStack.pop();
            idsInPath.add(nodeId);

            spreadLogic.apply(node(nodeId))
                    .filter(fromId -> !idsInPath.contains(fromId))
                    .forEach(accNodeIdStack::push);
        }

        return idsInPath;
    }

    public Iterator<ScNode<NK, NV, EK, EV>> scIterator() {
        return new ScIterator<>(nodes);
    }

    public Stream<ScNode<NK, NV, EK, EV>> scNodeStream() {
        return StreamSupport.stream(new ParallelSpliterator<>(scIterator(), Spliterator.ORDERED), false);
    }

    public Iterator<List<GraphNode<NK, NV, EK, EV>>> leafBatchIterator() {
        return new LeafBatchIterator<>(nodes, node -> node.outcomeStream().map(GraphEdge::getTo));
    }

    public Iterator<List<GraphNode<NK, NV, EK, EV>>> leafBatchReverseIterator() {
        return new LeafBatchIterator<>(nodes, node -> node.incomeStream().map(GraphEdge::getFrom));
    }

    public Stream<List<GraphNode<NK, NV, EK, EV>>> leafBatchStream() {
        return StreamSupport.stream(new ParallelSpliterator<>(leafBatchIterator(), Spliterator.ORDERED), false);
    }

    public Stream<List<GraphNode<NK, NV, EK, EV>>> leafBatchReverseStream() {
        return StreamSupport.stream(new ParallelSpliterator<>(leafBatchReverseIterator(), Spliterator.ORDERED), false);
    }

    public DirectGraph<Long, ScNode<NK, NV, EK, EV>, Long, Long> createScGraph() {
        return createScGraph(nodes);
    }

    public DirectGraph<Long, ScNode<NK, NV, EK, EV>, Long, Long> createScGraph(Predicate<GraphNode<NK, NV, EK, EV>> nodeSelector) {
        Map<NK, GraphNode<NK, NV, EK, EV>> subNodes = nodes.values().stream()
                .filter(nodeSelector)
                .collect(Collectors.toMap(GraphNode::getId, Function.identity()));

        return createScGraph(subNodes);
    }

    public DirectGraph<Long, ScNode<NK, NV, EK, EV>, Long, Long> createScGraph(Map<NK, GraphNode<NK, NV, EK, EV>> subNodes) {
        DirectGraph<Long, ScNode<NK, NV, EK, EV>, Long, Long> scGraph = new DirectGraph<>(nodeCount());

        Map<NK, Long> nodeToScMap = fillScNodeIntoScGraph(scGraph, subNodes);
        fillScEdgeIntoScGraph(scGraph, nodeToScMap, subNodes);

        return scGraph;
    }

    private void fillScEdgeIntoScGraph(DirectGraph<Long, ScNode<NK, NV, EK, EV>, Long, Long> scGraph,
                                       Map<NK, Long> nodeToScMap,
                                       Map<NK, GraphNode<NK, NV, EK, EV>> subNodes) {
        AtomicLong idHolder = new AtomicLong(0L);
        subNodes.values().stream()
                .flatMap(GraphNode::outcomeStream)
                .map(edge -> new GraphEdge<>(idHolder.incrementAndGet(), nodeToScMap.get(edge.getFrom()), nodeToScMap.get(edge.getTo()), 0L))
                .filter(edge -> edge.getFrom() != null && edge.getTo() != null && !edge.getFrom().equals(edge.getTo()))
                .forEach(edge -> {
                    GraphNode<Long, ScNode<NK, NV, EK, EV>, Long, Long> from = scGraph.node(edge.getFrom());
                    if (!from.outcomeStream().anyMatch(e -> e.getTo().equals(edge.getTo()))) {
                        from.withOutcome(edge);
                    }
                });
        scGraph.initIncomeWithOutcome();
    }

    private Map<NK, Long> fillScNodeIntoScGraph(DirectGraph<Long, ScNode<NK, NV, EK, EV>, Long, Long> scGraph,
                                                Map<NK, GraphNode<NK, NV, EK, EV>> subNodes) {
        Map<NK, Long> nodeToScMap = new HashMap<>(subNodes.size());
        AtomicLong idHolder = new AtomicLong(0L);
        Iterator<ScNode<NK, NV, EK, EV>> scIt = new ScIterator<>(subNodes);
        while (scIt.hasNext()) {
            ScNode<NK, NV, EK, EV> scNode = scIt.next();
            GraphNode<Long, ScNode<NK, NV, EK, EV>, Long, Long> newGraphNode =
                    new GraphNode<Long, ScNode<NK, NV, EK, EV>, Long, Long>(idHolder.incrementAndGet())
                            .withValue(scNode);
            scGraph.acceptNode(newGraphNode);
            for (GraphNode<NK, NV, EK, EV> node : scNode.nodeList()) {
                nodeToScMap.put(node.getId(), newGraphNode.getId());
            }
        }
        return nodeToScMap;
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    /**
     * distribute something to other node by income edges
     * @param distributeLogic (source,target)-&gt;target is changed or not
     */
    public void distributeByIncome(BiFunction<NV, NV, Boolean> distributeLogic) {
        Set<NK> changedNodes = new HashSet<>(nodes.keySet());
        while (!changedNodes.isEmpty()) {
            changedNodes = distributeInfoToParent(changedNodes, nodes, distributeLogic);
        }
    }

    private Set<NK> distributeInfoToParent(Set<NK> nodeIds,
                                           Map<NK, GraphNode<NK, NV, EK, EV>> nodeMap,
                                           BiFunction<NV, NV, Boolean> distributeLogic) {
        Set<NK> changedNodes = new HashSet<>();
        for (NK nodeId : nodeIds) {
            GraphNode<NK, NV, EK, EV> node = nodeMap.get(nodeId);
            node.incomeStream()
                    .forEach(income -> {
                        GraphNode<NK, NV, EK, EV> from = nodeMap.get(income.getFrom());
                        if (distributeLogic.apply(node.getValue(), from.getValue())) {
                            changedNodes.add(from.getId());
                        }
                    });
        }
        return changedNodes;
    }

    public void removeEdge(GraphEdge<EK, EV, NK> edge) {
        nodes.get(edge.getFrom())
                .getOutcomes().remove(edge.getId());
        GraphNode<NK, NV, EK, EV> to = nodes.get(edge.getTo());
        Map<EK, GraphEdge<EK, EV, NK>> incomes = to.getIncomes();
        if (incomes != null) {
            incomes.remove(edge.getId());
        }
    }

    /**
     * merge subNode into mainNode
     * @param mainNode node that will be hold in graph
     * @param subNode node that will be removed from graph
     */
    public void mergeNode(GraphNode<NK, NV, EK, EV> mainNode, GraphNode<NK, NV, EK, EV> subNode) {
        NK mainId = mainNode.getId();
        Collection<GraphEdge<EK, EV, NK>> outcomes = subNode.getOutcomes() == null ? Collections.emptyList() : subNode.getOutcomes().values();
        for (GraphEdge<EK, EV, NK> edge : new ArrayList<>(outcomes)) {
            removeEdge(edge);

            GraphEdge<EK, EV, NK> newEdge = new GraphEdge<>(edge.getId(), mainId, edge.getTo(), edge.getValue());
            acceptEdge(newEdge);
        }

        Collection<GraphEdge<EK, EV, NK>> incomes = subNode.getIncomes() == null ? Collections.emptyList() : subNode.getIncomes().values();
        for (GraphEdge<EK, EV, NK> edge : new ArrayList<>(incomes)) {
            removeEdge(edge);

            GraphEdge<EK, EV, NK> newEdge = new GraphEdge<>(edge.getId(), edge.getFrom(), mainId, edge.getValue());
            acceptEdge(newEdge);
        }

        nodes.remove(subNode.getId());
    }
}
