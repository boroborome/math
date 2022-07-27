package com.happy3w.math.graph;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Getter
public class GraphNode<NK, NV, EK, EV> {
    private NK id;
    private Map<EK, GraphEdge<EK, EV, NK>> incomes;
    private Map<EK, GraphEdge<EK, EV, NK>> outcomes;

    @Setter
    private NV value;

    public GraphNode(NK id) {
        this.id = id;
    }

    public Stream<EV> outcomeValues() {
        return outcomes == null
                ? Stream.empty()
                : outcomes.values().stream().map(GraphEdge::getValue);
    }

    public GraphNode<NK, NV, EK, EV> withOutcome(GraphEdge<EK, EV, NK> edge) {
        if (outcomes == null) {
            outcomes = new HashMap<>();
        }
        outcomes.put(edge.getId(), edge);
        return this;
    }

    public GraphNode<NK, NV, EK, EV> withIncome(GraphEdge<EK, EV, NK> edge) {
        if (incomes == null) {
            incomes = new HashMap<>();
        }
        incomes.put(edge.getId(), edge);
        return this;
    }

    public Stream<GraphEdge<EK, EV, NK>> outcomeStream() {
        return outcomes == null ? Stream.empty() : outcomes.values().stream();
    }

    public Stream<GraphEdge<EK, EV, NK>> incomeStream() {
        return incomes == null ? Stream.empty() : incomes.values().stream();
    }

    public void removeOutcome(GraphEdge<EK, EV, NK> outcome) {
        if (outcomes == null || outcome == null) {
            return;
        }
        outcomes.remove(outcome.getId());
    }

    public void removeIncome(GraphEdge<EK, EV, NK> income) {
        if (incomes == null || income == null) {
            return;
        }
        incomes.remove(income.getId());
    }
}
