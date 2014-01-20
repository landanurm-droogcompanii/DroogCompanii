package ru.droogcompanii.application.data.searchable_sortable.filter;

import ru.droogcompanii.application.data.searchable_sortable.SearchableSortable;

/**
 * Created by ls on 15.01.14.
 */
public interface Filter<T> {
    void includeIn(SearchableSortable<T> searchableSortable);
}
