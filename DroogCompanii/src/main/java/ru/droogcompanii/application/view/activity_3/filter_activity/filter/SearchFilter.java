package ru.droogcompanii.application.view.activity_3.filter_activity.filter;

import ru.droogcompanii.application.data.SearchableSortable;

/**
 * Created by ls on 16.01.14.
 */
public abstract class SearchFilter<T> implements Filter<T>, SearchableSortable.SearchCriteria<T> {
    @Override
    public void includeIn(SearchableSortable<T> searchableSortable) {
        searchableSortable.addSearchCriteria(this);
    }
}
