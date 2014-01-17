package ru.droogcompanii.application.util;

/**
 * Created by ls on 17.01.14.
 */
public class MinMax<T extends Comparable<T>> {

    private T min;
    private T max;

    public void add(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot add <null>");
        }
        if (min == null) {
            min = value;
            max = value;
        } else if (value.compareTo(min) < 0) {
            min = value;
        } else if (value.compareTo(max) > 0) {
            max = value;
        }
    }

    public T min() {
        return min;
    }

    public T max() {
        return max;
    }
}
