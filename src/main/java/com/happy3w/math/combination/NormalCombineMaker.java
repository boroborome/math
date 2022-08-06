package com.happy3w.math.combination;

import com.happy3w.java.ext.NeedFindIterator;
import com.happy3w.java.ext.NullableOptional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class NormalCombineMaker<T> {
    private final List<T> values;

    public NormalCombineMaker(List<T> values) {
        this.values = values;
    }

    public Iterator<List<T>> combineIterator(int count) {
        return new CombineIterator<>(values, count);
    }

    public Stream<List<T>> combineStream(int count) {
        Iterator<List<T>> it = combineIterator(count);
        return StreamSupport.stream(Spliterators.spliterator(it, Long.MAX_VALUE, 0), false);
    }


    private static class CombineIterator<T> extends NeedFindIterator<List<T>> {
        private final List<T> values;

        private int[] indexes;

        protected CombineIterator(List<T> values, int count) {
            this.values = values;

            indexes = initIndexes(count);
            nextItem = createCombineResult();
            status = IteratorStatus.found;
        }

        private int[] initIndexes(int count) {
            int[] indexes = new int[count];
            for (int i = 0; i < count; i++) {
                indexes[i] = i;
            }
            return indexes;
        }

        private boolean increaseIndexes() {
            int maxPos = values.size() - indexes.length;

            int curIndex = indexes.length - 1;
            for (; curIndex >= 0; curIndex--) {
                int nextPos = indexes[curIndex] + 1;
                if (nextPos <= maxPos + curIndex) {
                    break;
                }
            }

            if (curIndex < 0) {
                return false;
            }

            for (int curPos = indexes[curIndex]; curIndex < indexes.length; curIndex++) {
                curPos++;
                indexes[curIndex] = curPos;
            }

            return true;
        }

        @Override
        protected NullableOptional<List<T>> findNext() {
            boolean hasNext = increaseIndexes();
            if (hasNext) {
                return NullableOptional.of(createCombineResult());
            }
            return NullableOptional.empty();
        }

        private List<T> createCombineResult() {
            List<T> combineResult = new ArrayList<>();
            for (int index : indexes) {
                combineResult.add(values.get(index));
            }
            return combineResult;
        }
    }
}
