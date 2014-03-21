package ru.droogcompanii.application.util;

/**
 * Created by ls on 21.03.14.
 */
public interface OnEachIterable<T> {
    void forEach(OnEachHandler<T> onEachHandler);
}
