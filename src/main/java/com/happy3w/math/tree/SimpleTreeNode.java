package com.happy3w.math.tree;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleTreeNode<T> implements WritableTreeNode<T> {
    private T data;
    private List<TreeNode<T>> subNodes;

    public SimpleTreeNode(T data) {
        this.data = data;
    }

    public <NT extends TreeNode<T>> void addSubNode(NT node) {
        if (subNodes == null) {
            subNodes = new ArrayList<>();
        }
        subNodes.add(node);
    }
}
