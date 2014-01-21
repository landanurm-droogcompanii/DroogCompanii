package ru.droogcompanii.application.data.searchable_sortable_listing.filter;

import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableSortableListing;

/**
 * Created by ls on 16.01.14.
 */
public abstract class SearchFilter<T> implements Filter<T>, SearchableSortableListing.SearchCriterion<T> {
    @Override
    public void includeIn(SearchableSortableListing<T> searchableSortableListing) {
        searchableSortableListing.addSearchCriterion(this);
    }

    @Override
    public abstract boolean meetCriterion(T obj);
}
