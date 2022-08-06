package com.happy3w.math.graph;

import java.util.stream.Stream;

/**
 * Strong connection Node
 * @param <NK> Node Key Type
 * @param <NV> Node Value Type
 * @param <EK> Edge Key Type
 * @param <EV> Edge Value Type
 */
public interface ScNode<NK, NV, EK, EV> {
    Stream<NK> idStream();
}
