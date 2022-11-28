package com.happy3w.math.graph;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GraphEdge<EK, EV, NK> {
    private EK id;
    private NK from;
    private NK to;

    private EV value;

    public GraphEdge<EK, EV, NK> cloneEdge() {
        return new GraphEdge<>(id, from, to, value);
    }
}
