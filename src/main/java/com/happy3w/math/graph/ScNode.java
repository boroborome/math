package com.happy3w.math.graph;

import java.util.List;
import java.util.stream.Stream;

/**
 * Strong connection Node
 * @param <NK> Node Key Type
 * @param <NV> Node Value Type
 * @param <EK> Edge Key Type
 * @param <EV> Edge Value Type
 */
public interface ScNode<NK, NV, EK, EV> {
    List<GraphNode<NK, NV, EK, EV>> nodeList();

    default Stream<GraphNode<NK, NV, EK, EV>> nodeStream() {
        return nodeList().stream();
    }

    default Stream<NK> idStream() {
        return nodeStream().map(GraphNode::getId);
    }
}
