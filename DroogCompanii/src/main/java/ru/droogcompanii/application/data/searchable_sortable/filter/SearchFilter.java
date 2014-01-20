package ru.droogcompanii.application.data.searchable_sortable.filter;

import ru.droogcompanii.application.data.searchable_sortable.SearchableSortable;

/**
 * Created by ls on 16.01.14.
 */
public abstract class SearchFilter<T> implements Filter<T>, SearchableSortable.SearchCriteria<T> {
    @Override
    public void includeIn(SearchableSortable<T> searchableSortable) {
        searchableSortable.addSearchCriteria(this);
    }
}
