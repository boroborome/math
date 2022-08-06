package com.happy3w.math.graph;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class CombineScNode<NK, NV, EK, EV> implements ScNode<NK, NV, EK, EV> {
    private final List<GraphNode<NK, NV, EK, EV>> graphNodes;

    public CombineScNode(List<GraphNode<NK, NV, EK, EV>> graphNodes) {
        this.graphNodes = graphNodes;
    }

    @Override
    public List<GraphNode<NK, NV, EK, EV>> nodeList() {
        return graphNodes == null || graphNodes.isEmpty()
                ? Collections.EMPTY_LIST
                : graphNodes;
    }
}
