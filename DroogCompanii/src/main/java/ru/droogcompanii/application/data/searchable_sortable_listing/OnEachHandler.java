package ru.droogcompanii.application.data.searchable_sortable_listing;

/**
 * Created by ls on 27.01.14.
 */
public interface OnEachHandler<T> {
    void onEach(T each, boolean meetsCriteria);
}
