package com.happy3w.math.graph;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public class SingleScNode<NK, NV, EK, EV> implements ScNode<NK, NV, EK, EV> {
    private final GraphNode<NK, NV, EK, EV> graphNode;

    public SingleScNode(GraphNode<NK, NV, EK, EV> graphNode) {
        this.graphNode = graphNode;
    }

    @Override
    public Stream<NK> idStream() {
        return nodeStream().map(GraphNode::getId);
    }

    @Override
    public Stream<GraphNode<NK, NV, EK, EV>> nodeStream() {
        return graphNode == null ? Stream.empty() : Stream.of(graphNode);
    }

    public static <EV, NV, NK, EK> SingleScNode<NK, NV, EK, EV> from(GraphNode<NK, NV, EK, EV> graphNode) {
        return new SingleScNode<>(graphNode);
    }

}
