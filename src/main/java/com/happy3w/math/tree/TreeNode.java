package com.happy3w.math.tree;

import java.util.List;
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
}
