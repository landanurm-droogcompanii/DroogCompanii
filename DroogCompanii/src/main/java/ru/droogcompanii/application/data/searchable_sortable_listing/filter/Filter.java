package ru.droogcompanii.application.data.searchable_sortable_listing.filter;

import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableSortableListing;

/**
 * Created by ls on 15.01.14.
 */
public interface Filter<T> {
    void includeIn(SearchableSortableListing<T> searchableSortableListing);
}
