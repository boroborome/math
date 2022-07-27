package com.happy3w.math.tree;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface TreeNode<T> {
    T getData();

    default boolean beLeafNode() {
        List<TreeNode<T>> subNodes = getSubNodes();
        return subNodes == null || subNodes.isEmpty();
    }

    List<TreeNode<T>> getSubNodes();

    default Stream<TreeNode<T>> nodeStream() {
        return beLeafNode()
                ? Stream.of(this)
                : Stream.concat(
                    Stream.of(this),
                    getSubNodes().stream()
                            .flatMap(TreeNode::nodeStream)
        );
    }

    default TreeNode<T> cloneNode() {
        List<TreeNode<T>> subNodes = getSubNodes();
        List<TreeNode<T>> newSubNodes = subNodes == null
                ? null
                : subNodes.stream().map(TreeNode::cloneNode).collect(Collectors.toList());
        return cloneWithSubNodes(newSubNodes);
    }

    TreeNode<T> cloneWithSubNodes(List<TreeNode<T>> newSubNodes);
}
