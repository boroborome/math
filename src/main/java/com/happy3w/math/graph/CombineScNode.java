package com.happy3w.math.graph;

import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

@Getter
public class CombineScNode<NK, NV, EK, EV> implements ScNode<NK, NV, EK, EV> {
    private final List<GraphNode<NK, NV, EK, EV>> graphNodes;

    public CombineScNode(List<GraphNode<NK, NV, EK, EV>> graphNodes) {
        this.graphNodes = graphNodes;
    }

    @Override
    public Stream<NK> idStream() {
        return nodeStream().map(GraphNode::getId);
    }

    @Override
    public Stream<GraphNode<NK, NV, EK, EV>> nodeStream() {
        return graphNodes == null || graphNodes.isEmpty()
                ? Stream.empty()
                : graphNodes.stream();
    }
}
