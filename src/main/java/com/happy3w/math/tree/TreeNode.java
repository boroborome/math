package com.happy3w.math.tree;

import java.util.List;

public interface TreeNode<T> {
    T getData();

    default boolean leafNode() {
        List<TreeNode<T>> subNodes = getSubNodes();
        return subNodes == null || subNodes.isEmpty();
    }

    List<TreeNode<T>> getSubNodes();
}
