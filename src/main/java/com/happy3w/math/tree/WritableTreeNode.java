package com.happy3w.math.tree;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface WritableTreeNode<T> extends TreeNode<T> {
    void setSubNodes(List<TreeNode<T>> subNodes);

    default WritableTreeNode<T> filterNodes(Predicate<TreeNode<T>> checker) {
        Queue<TreeNode<T>> queue = new LinkedList<>();
        queue.add(this);

        while (!queue.isEmpty()) {
            TreeNode<T> curNode = queue.poll();
            if (curNode.getSubNodes() == null) {
                continue;
            }
            List<TreeNode<T>> newSubNodes = filterNodes(curNode.getSubNodes(), checker, queue)
                    .collect(Collectors.toList());
            if (!Objects.equals(curNode.getSubNodes(), newSubNodes)) {
                if (curNode instanceof WritableTreeNode) {
                    ((WritableTreeNode) curNode).setSubNodes(newSubNodes);
                } else {
                    throw new UnsupportedOperationException("Failed to modify readonly tree node:" + curNode.getData()
                            + " node type:" + curNode.getClass());
                }

            }
        }
        return this;
    }

    default Stream<TreeNode<T>> filterNodes(List<TreeNode<T>> nodeList, Predicate<TreeNode<T>> checker, Queue<TreeNode<T>> queue) {
        return nodeList.stream()
                .flatMap(subItem -> {
                    if (checker.test(subItem)) {
                        queue.add(subItem);
                        return Stream.of(subItem);
                    } else if (subItem.beLeafNode()) {
                        return Stream.empty();
                    } else {
                        return filterNodes(subItem.getSubNodes(), checker, queue);
                    }
                });
    }
}
