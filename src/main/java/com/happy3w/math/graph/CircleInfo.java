package com.happy3w.math.graph;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;

public class CircleInfo<NK> {
    @Getter
    private List<NK> pathFromParent;

    @Getter
    private Set<NK> circle;
    private Stack<NK> outNodeStack = new Stack<>();

    public static <NK> CircleInfo<NK> createNewCircle(List<NK> idPath, NK curId, Function<NK, GraphNode<NK, ?, ?, ?>> nodeMapper) {
        int index = idPath.indexOf(curId);
        if (index < 0) {
            return null;
        }
        CircleInfo<NK> info = new CircleInfo<>();
        info.pathFromParent = new ArrayList<>(idPath.subList(0, index));
        info.circle = new HashSet<>(idPath.subList(index, idPath.size()));

        info.rebuildOutNodes(nodeMapper);
        return info;
    }

    public static <NK> boolean mergeIntoBigCircle(List<NK> idPath, NK curId, List<CircleInfo<NK>> candidateCircles,
                                                  Function<NK, GraphNode<NK, ?, ?, ?>> nodeMapper) {
        for (int i = candidateCircles.size() - 1; i >= 0; i--) {
            CircleInfo curCircle = candidateCircles.get(i);
            if (curCircle.mergePath(curId, idPath)) {
                while (candidateCircles.size() > i + 1) {
                    int lastIndex = candidateCircles.size() - 1;
                    CircleInfo lastCircle = candidateCircles.get(lastIndex);
                    curCircle.mergeCircle(lastCircle);
                    candidateCircles.remove(lastIndex);
                }
                curCircle.rebuildOutNodes(nodeMapper);
                return true;
            }
        }
        return false;
    }

    private boolean mergePath(NK curId, List<NK> idPath) {
        if (circle.contains(curId)) {
            circle.addAll(idPath);
            return true;
        }

        int pos = pathFromParent.indexOf(curId);
        if (pos >= 0) {
            circle.addAll(idPath);
            circle.addAll(pathFromParent.subList(pos, pathFromParent.size()));
            pathFromParent = new ArrayList<>(pathFromParent.subList(0, pos));
            return true;
        }
        return false;
    }

    private void mergeCircle(CircleInfo<NK> otherCircle) {
        circle.addAll(otherCircle.circle);
        circle.addAll(otherCircle.pathFromParent);
    }

    private void rebuildOutNodes(Function<NK, GraphNode<NK, ?, ?, ?>> nodeMapper) {
        outNodeStack.clear();
        circle.stream()
                .map(nodeMapper)
                .flatMap(m -> m.outcomeStream().map(GraphEdge::getTo))
                .distinct()
                .filter(nodeCode -> nodeMapper.apply(nodeCode) != null && !circle.contains(nodeCode))
                .forEach(outNodeStack::push);
    }

    public NK nextNewStartNode() {
        return outNodeStack.isEmpty() ? null : outNodeStack.pop();
    }
}
