package ru.droogcompanii.application.data.searchable_sortable_listing;

/**
 * Created by ls on 05.02.14.
 */
public interface SearchResult<T> {
    T value();
    boolean meetsSearchCriteria();
}
