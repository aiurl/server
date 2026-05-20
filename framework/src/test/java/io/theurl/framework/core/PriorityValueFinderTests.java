package io.theurl.framework.core;

import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;

class PriorityValueFinderTests {
    @Test
    void testFind() {
        // Add test cases for PriorityValueFinder.find method
        PriorityQueue<Integer> values = new PriorityQueue<>();
        values.offer(1);
        values.offer(2);
        values.offer(3);
        values.offer(4);
        values.offer(5);

        Integer result = PriorityValueFinder.find(values, value -> value % 2 == 0, -1);
        assert result == 2 : "Expected 2, but got " + result;
    }
}
