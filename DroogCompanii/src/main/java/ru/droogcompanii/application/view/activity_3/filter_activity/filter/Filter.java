package ru.droogcompanii.application.view.activity_3.filter_activity.filter;

import ru.droogcompanii.application.data.SearchableSortable;

/**
 * Created by ls on 15.01.14.
 */
public interface Filter<T> {
    void includeIn(SearchableSortable<T> searchableSortable);
}
