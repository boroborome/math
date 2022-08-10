package com.happy3w.math.graph;

import com.happy3w.java.ext.NeedFindIterator;
import com.happy3w.java.ext.NullableOptional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LeafBatchIterator<NK, NV, EK, EV> extends NeedFindIterator<List<GraphNode<NK, NV, EK, EV>>> {
    private final Map<NK, GraphNode<NK, NV, EK, EV>> nodesToEnum;

    public LeafBatchIterator(Map<NK, GraphNode<NK, NV, EK, EV>> nodesToEnum) {
        this.nodesToEnum = new HashMap<>(nodesToEnum);
    }

    @Override
    protected NullableOptional<List<GraphNode<NK, NV, EK, EV>>> findNext() {
        if (nodesToEnum.isEmpty()) {
            return NullableOptional.empty();
        }

        List<GraphNode<NK, NV, EK, EV>> leafBatch = nodesToEnum.values()
                .stream()
                .filter(node -> !node.outcomeStream()
                        .anyMatch(edge -> nodesToEnum.get(edge.getTo()) != null)
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
