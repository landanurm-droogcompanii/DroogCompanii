package ru.droogcompanii.application.data.searchable_sortable_listing.filter;

import java.util.Comparator;

import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableSortableListing;

/**
 * Created by ls on 16.01.14.
 */
public abstract class SortingFilter<T> implements Filter<T>, Comparator<T> {
    @Override
    public void includeIn(SearchableSortableListing<T> searchableSortableListing) {
        searchableSortableListing.addComparator(this);
    }

    @Override
    public abstract int compare(T obj1, T obj2);
}
