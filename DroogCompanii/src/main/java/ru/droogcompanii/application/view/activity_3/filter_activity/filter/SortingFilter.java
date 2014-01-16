package ru.droogcompanii.application.view.activity_3.filter_activity.filter;

import java.util.Comparator;

import ru.droogcompanii.application.data.SearchableSortable;

/**
 * Created by ls on 16.01.14.
 */
public abstract class SortingFilter<T> implements Filter<T>, Comparator<T> {
    @Override
    public void includeIn(SearchableSortable<T> searchableSortable) {
        searchableSortable.addComparator(this);
    }
}
