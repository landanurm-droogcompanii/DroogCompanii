package ru.droogcompanii.application.data.searchable_sortable_listing;

import java.io.Serializable;

/**
 * Created by ls on 06.02.14.
 */
public class SearchResultImpl<T> implements SearchResult<T>, Serializable {
    private final boolean meetsCriteria;
    private final T value;

    public SearchResultImpl(T value, boolean meetsCriteria) {
        this.value = value;
        this.meetsCriteria = meetsCriteria;
    }

    @Override
    public T value() {
        return value;
    }

    @Override
    public boolean meetsSearchCriteria() {
        return meetsCriteria;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SearchResult<T> other = (SearchResult<T>) obj;
        return (value().equals(other.value()) &&
                meetsSearchCriteria() == other.meetsSearchCriteria());
    }

    @Override
    public int hashCode() {
        return value().hashCode() + (meetsSearchCriteria() ? 1 : 0);
    }
}
