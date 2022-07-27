package com.happy3w.math.tree;

import java.util.List;
import java.util.function.BiFunction;
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

    default Stream<TreeNode<T>> allSubNodeStream() {
        return beLeafNode()
                ? Stream.empty()
                : getSubNodes().stream()
                        .flatMap(TreeNode::nodeStream);
    }

    default <R> Stream<R> allSubNodeStream(BiFunction<TreeNode<T>, TreeNode<T>, R> itemCreator) {
        List<TreeNode<T>> subNodes = getSubNodes();

        if (subNodes == null) {
            return Stream.empty();
        }
        return subNodes.stream()
                .flatMap(item -> Stream.concat(Stream.of(itemCreator.apply(item, this)), item.allSubNodeStream(itemCreator)));
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
