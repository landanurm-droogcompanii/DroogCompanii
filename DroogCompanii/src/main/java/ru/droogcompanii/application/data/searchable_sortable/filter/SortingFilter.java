package ru.droogcompanii.application.data.searchable_sortable.filter;

import java.util.Comparator;

import ru.droogcompanii.application.data.searchable_sortable.SearchableSortable;

/**
 * Created by ls on 16.01.14.
 */
public abstract class SortingFilter<T> implements Filter<T>, Comparator<T> {
    @Override
    public void includeIn(SearchableSortable<T> searchableSortable) {
        searchableSortable.addComparator(this);
    }
}
