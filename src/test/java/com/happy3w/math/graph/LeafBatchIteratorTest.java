package com.happy3w.math.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class LeafBatchIteratorTest {

    @Test
    void should_enum_success_with_tree() {
        DirectGraph<String, String, Long, Long> callGraph = GraphTestUtil.createCallGraph(new String[]{"M1->M2", "M1->M3", "M2->M4"});
        List<List<String>> batchList = callGraph.leafBatchStream()
                .map(batch -> batch.stream()
                        .map(node -> node.getId())
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        Assertions.assertEquals(
                Arrays.asList(
                        Arrays.asList("M3", "M4"),
                        Arrays.asList("M2"),
                        Arrays.asList("M1")
                ),
                batchList);
    }
}