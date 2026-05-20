package io.theurl.framework.core;

import java.util.PriorityQueue;
import java.util.function.Predicate;

/**
 * Utility class for finding a value in a priority queue based on a filter predicate.
 * This class provides a method to search through a priority queue and return the first value that matches the given filter.
 * If no matching value is found, it returns a specified default value.
 */
public class PriorityValueFinder {

    /**
     * Finds the first value in the priority queue that matches the given filter.
     * If no matching value is found, returns the default value.
     *
     * @param values       the priority queue to search
     * @param filter       the filter predicate to apply to each value
     * @param defaultValue the value to return if no matching value is found
     * @param <T>          the type of values in the priority queue
     * @return the first matching value or the default value if none is found
     */
    public static <T> T find(PriorityQueue<T> values, Predicate<T> filter, T defaultValue) {

        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("Values queue cannot be null  or empty");
        }

        if (filter == null) {
            throw new IllegalArgumentException("Filter queue cannot be null");
        }

        while (!values.isEmpty()) {
            T value = values.poll();
            if (filter.test(value)) {
                return value;
            }
        }
        return defaultValue;
    }
}
