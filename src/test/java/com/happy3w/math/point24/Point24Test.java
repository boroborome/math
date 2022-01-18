package com.happy3w.math.point24;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.MessageFormat;
import java.util.stream.Stream;

public class Point24Test {

    private static Stream<Point24Strategy> allStrategy() {
        return Stream.of(new OperFlowStrategy(),
                new BinTreeStrategy());
    }

    private void testNumber(int[] numbers, int expectResultCount, Point24Strategy point24Strategy) {
        long resultCount = point24Strategy.judge(numbers)
//                .peek(exp -> System.out.println(exp))
                .count();
        System.out.println(MessageFormat.format("Total case count:{0}", point24Strategy.getJudgedExpCount()));
        System.out.println(MessageFormat.format("Total result count:{0}", resultCount));
        Assertions.assertEquals(expectResultCount, resultCount);
    }

    @ParameterizedTest
    @MethodSource("allStrategy")
    public void should_judge_normal_success(Point24Strategy point24Strategy) {
        testNumber(new int[]{1, 7, 4, 8}, 15, point24Strategy);
    }

    @ParameterizedTest
    @MethodSource("allStrategy")
    public void should_judge_all_same_success(Point24Strategy point24Strategy) {
        testNumber(new int[]{6, 6, 6, 6}, 4, point24Strategy);
    }
}
