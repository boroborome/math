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
    private final DirectGraph<NK, NV, EK, EV> graph;
    private final Map<NK, GraphNode<NK, NV, EK, EV>> nodesToDeal;

    private Stack<CircleInfo<NK>> candidateCircles = new Stack<>();
    private Stack<NK> keyPath = new Stack<>();

    public ScIterator(DirectGraph<NK, NV, EK, EV> graph) {
        this.graph = graph;
        this.nodesToDeal = new HashMap<>(graph.getNodes());
    }

    @Override
    protected NullableOptional<ScNode<NK, NV, EK, EV>> findNext() {
        ScNode<NK, NV, EK, EV> leafHolder = findLeafHolder();
        if (leafHolder == null) {
            return NullableOptional.empty();
        } else {
            leafHolder.keys()
                    .forEach(nodesToDeal::remove);
            return NullableOptional.of(leafHolder);
        }
    }

    private ScNode<NK, NV, EK, EV> findLeafHolder() {
        while (!nodesToDeal.isEmpty()) {
            SingleScNode<NK, NV, EK, EV> curHolder;
            if (!keyPath.isEmpty()) {
                NK lastKey = keyPath.pop();
                GraphNode<NK, NV, EK, EV> graphNode = nodesToDeal.get(lastKey);
                curHolder = SingleScNode.from(graphNode);
            } else if (!candidateCircles.isEmpty()) {
                CircleInfo<NK> curCircle = candidateCircles.pop();
                NK newStartMethod = curCircle.nextNewStartNode();
                if (newStartMethod == null) {
                    keyPath.addAll(curCircle.getPathFromParent());
                    return new CombineScNode(codeToHolder(curCircle.getCircle(), nodesToDeal::get));
                }
                candidateCircles.push(curCircle);
                GraphNode<NK, NV, EK, EV> graphNode = nodesToDeal.get(newStartMethod);
                if (graphNode == null) {
                    continue;
                }
                curHolder = SingleScNode.from(graphNode);
            } else {
                GraphNode<NK, NV, EK, EV> graphNode = nodesToDeal.values().iterator().next();
                curHolder = SingleScNode.from(graphNode);
            }

            NK curCode = curHolder.getGraphNode().getId();
            CircleInfo<NK> curCircle = CircleInfo.createNewCircle(keyPath, curCode, nodesToDeal::get);
            if (curCircle != null) {
                keyPath.clear();
                candidateCircles.push(curCircle);
            } else if (CircleInfo.mergeIntoBigCircle(keyPath, curCode, candidateCircles, nodesToDeal::get)) {
                keyPath.clear();
            } else {
                SingleScNode<NK, NV, EK, EV> calleeHolder = pickAnyCallee(curHolder.getGraphNode(), nodesToDeal::get);
                if (calleeHolder == null) {
                    return curHolder;
                }

                keyPath.push(curCode);
                keyPath.push(calleeHolder.getGraphNode().getId());
            }
        }
        return null;
    }
    private SingleScNode<NK, NV, EK, EV> pickAnyCallee(GraphNode<NK, NV, EK, EV> node,
                                                       Function<NK, GraphNode<NK, NV, EK, EV>> holderMapper) {
        return node.outcomeStream()
                .map(edge -> holderMapper.apply(edge.getTo()))
                .filter(Objects::nonNull)
                .map(SingleScNode::from)
                .findFirst()
                .orElse(null);
    }

    private List<GraphNode<NK, NV, EK, EV>> codeToHolder(Collection<NK> codes,
                                                         Function<NK, GraphNode<NK, NV, EK, EV>> codeHolderTranslator) {
        return codes.stream()
                .map(codeHolderTranslator)
                .collect(Collectors.toList());
    }
}
