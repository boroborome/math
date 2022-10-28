package com.happy3w.math.graph;

import com.happy3w.java.ext.NeedFindIterator;
import com.happy3w.java.ext.NullableOptional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LeafBatchIterator<NK, NV, EK, EV> extends NeedFindIterator<List<GraphNode<NK, NV, EK, EV>>> {
    private final Function<GraphNode<NK, NV, EK, EV>, Stream<NK>> subNodeDetector;
    private final Map<NK, GraphNode<NK, NV, EK, EV>> nodesToEnum;

    public LeafBatchIterator(Map<NK, GraphNode<NK, NV, EK, EV>> nodesToEnum) {
        this(nodesToEnum, node -> node.outcomeStream().map(GraphEdge::getTo));
    }

    public LeafBatchIterator(Map<NK, GraphNode<NK, NV, EK, EV>> nodesToEnum, Function<GraphNode<NK, NV, EK, EV>, Stream<NK>> subNodeDetector) {
        this.nodesToEnum = new HashMap<>(nodesToEnum);
        this.subNodeDetector = subNodeDetector;
    }

    @Override
    protected NullableOptional<List<GraphNode<NK, NV, EK, EV>>> findNext() {
        if (nodesToEnum.isEmpty()) {
            return NullableOptional.empty();
        }

        List<GraphNode<NK, NV, EK, EV>> leafBatch = nodesToEnum.values()
                .stream()
                .filter(node -> !subNodeDetector.apply(node)
                        .anyMatch(subNode -> nodesToEnum.get(subNode) != null)
                ).collect(Collectors.toList());

        if (leafBatch.isEmpty()) {
            throw new RuntimeException("There is a circle in graph. Please break it before call leaf batch.");
        }

        for (GraphNode<NK, NV, EK, EV> node : leafBatch) {
            nodesToEnum.remove(node.getId());
        }

        return NullableOptional.of(leafBatch);
    }
}
