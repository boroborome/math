package com.happy3w.math.graph;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;

// TODO: rename to Strong connection Node Info
public class CircleInfo<NK> {
    @Getter
    private List<NK> pathFromParent;

    @Getter
    private Set<NK> circle;
    private Stack<NK> outNodeStack = new Stack<>();

    public static <NK> CircleInfo<NK> createNewCircle(List<NK> codePath, NK curCode, Function<NK, GraphNode<NK, ?, ?, ?>> holderMapper) {
        int index = codePath.indexOf(curCode);
        if (index < 0) {
            return null;
        }
        CircleInfo<NK> info = new CircleInfo<>();
        info.pathFromParent = new ArrayList<>(codePath.subList(0, index));
        info.circle = new HashSet<>(codePath.subList(index, codePath.size()));

        info.rebuildOutNodes(holderMapper);
        return info;
    }

    public static <NK> boolean mergeIntoBigCircle(List<NK> codePath, NK curCode, List<CircleInfo<NK>> candidateCircles,
                                                  Function<NK, GraphNode<NK, ?, ?, ?>> holderMapper) {
        for (int i = candidateCircles.size() - 1; i >= 0; i--) {
            CircleInfo curCircle = candidateCircles.get(i);
            if (curCircle.mergePath(curCode, codePath)) {
                while (candidateCircles.size() > i + 1) {
                    int lastIndex = candidateCircles.size() - 1;
                    CircleInfo lastCircle = candidateCircles.get(lastIndex);
                    curCircle.mergeCircle(lastCircle);
                    candidateCircles.remove(lastIndex);
                }
                curCircle.rebuildOutNodes(holderMapper);
                return true;
            }
        }
        return false;
    }

    private boolean mergePath(NK curCode, List<NK> codePath) {
        if (circle.contains(curCode)) {
            circle.addAll(codePath);
            return true;
        }

        int pos = pathFromParent.indexOf(curCode);
        if (pos >= 0) {
            circle.addAll(codePath);
            circle.addAll(pathFromParent.subList(pos, pathFromParent.size()));
            pathFromParent = new ArrayList<>(pathFromParent.subList(0, pos));
            return true;
        }
        return false;
    }

    private void mergeCircle(CircleInfo otherCircle) {
        circle.addAll(otherCircle.circle);
        circle.addAll(otherCircle.pathFromParent);
    }

    private void rebuildOutNodes(Function<NK, GraphNode<NK, ?, ?, ?>> holderMapper) {
        outNodeStack.clear();
        circle.stream()
                .map(holderMapper)
                .flatMap(m -> m.outcomeStream().map(GraphEdge::getTo))
                .distinct()
                .filter(nodeCode -> holderMapper.apply(nodeCode) != null && !circle.contains(nodeCode))
                .forEach(outNodeStack::push);
    }

    public NK nextNewStartNode() {
        return outNodeStack.isEmpty() ? null : outNodeStack.pop();
    }
}
