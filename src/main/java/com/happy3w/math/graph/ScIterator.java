package com.happy3w.math.graph;

import com.happy3w.java.ext.NeedFindIterator;
import com.happy3w.java.ext.NullableOptional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ScIterator<NK, NV, EK, EV> extends NeedFindIterator<ScNode<NK, NV, EK, EV>> {
    private final Map<NK, GraphNode<NK, NV, EK, EV>> nodesToDeal;

    private Stack<CircleInfo<NK>> candidateCircles = new Stack<>();
    private Stack<NK> idPath = new Stack<>();

    public ScIterator(Map<NK, GraphNode<NK, NV, EK, EV>> nodes) {
        this.nodesToDeal = new HashMap<>(nodes);
    }

    @Override
    protected NullableOptional<ScNode<NK, NV, EK, EV>> findNext() {
        ScNode<NK, NV, EK, EV> leafNode = findLeafNode();
        if (leafNode == null) {
            return NullableOptional.empty();
        } else {
            leafNode.idStream()
                    .forEach(nodesToDeal::remove);
            return NullableOptional.of(leafNode);
        }
    }

    private ScNode<NK, NV, EK, EV> findLeafNode() {
        while (!nodesToDeal.isEmpty()) {
            SingleScNode<NK, NV, EK, EV> curScNode;
            if (!idPath.isEmpty()) {
                NK lastId = idPath.pop();
                GraphNode<NK, NV, EK, EV> graphNode = nodesToDeal.get(lastId);
                curScNode = SingleScNode.from(graphNode);
            } else if (!candidateCircles.isEmpty()) {
                CircleInfo<NK> curCircle = candidateCircles.pop();
                NK newStartNode = curCircle.nextNewStartNode();
                if (newStartNode == null) {
                    idPath.addAll(curCircle.getPathFromParent());
                    return new CombineScNode(idToNode(curCircle.getCircle(), nodesToDeal::get));
                }
                candidateCircles.push(curCircle);
                GraphNode<NK, NV, EK, EV> graphNode = nodesToDeal.get(newStartNode);
                if (graphNode == null) {
                    continue;
                }
                curScNode = SingleScNode.from(graphNode);
            } else {
                GraphNode<NK, NV, EK, EV> graphNode = nodesToDeal.values().iterator().next();
                curScNode = SingleScNode.from(graphNode);
            }

            NK curId = curScNode.getGraphNode().getId();
            CircleInfo<NK> curCircle = CircleInfo.createNewCircle(idPath, curId, nodesToDeal::get);
            if (curCircle != null) {
                idPath.clear();
                candidateCircles.push(curCircle);
            } else if (CircleInfo.mergeIntoBigCircle(idPath, curId, candidateCircles, nodesToDeal::get)) {
                idPath.clear();
            } else {
                SingleScNode<NK, NV, EK, EV> outNode = pickAnyOutNode(curScNode.getGraphNode(), nodesToDeal::get);
                if (outNode == null) {
                    return curScNode;
                }

                idPath.push(curId);
                idPath.push(outNode.getGraphNode().getId());
            }
        }
        return null;
    }
    private SingleScNode<NK, NV, EK, EV> pickAnyOutNode(GraphNode<NK, NV, EK, EV> node,
                                                        Function<NK, GraphNode<NK, NV, EK, EV>> nodeMapper) {
        return node.outcomeStream()
                .map(edge -> nodeMapper.apply(edge.getTo()))
                .filter(Objects::nonNull)
                .map(SingleScNode::from)
                .findFirst()
                .orElse(null);
    }

    private List<GraphNode<NK, NV, EK, EV>> idToNode(Collection<NK> ids,
                                                     Function<NK, GraphNode<NK, NV, EK, EV>> idNodeTranslator) {
        return ids.stream()
                .map(idNodeTranslator)
                .collect(Collectors.toList());
    }
}
