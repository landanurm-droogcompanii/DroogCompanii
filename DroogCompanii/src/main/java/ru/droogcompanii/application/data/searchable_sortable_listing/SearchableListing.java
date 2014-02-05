package ru.droogcompanii.application.data.searchable_sortable_listing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ls on 21.01.14.
 */
public class SearchableListing<T> implements Serializable {

    private final List<SearchCriterion<T>> searchCriteria;

    protected final List<T> elements;


    public static <T> SearchableListing<T> newInstance(Collection<T> elements) {
        return new SearchableListing<T>(elements);
    }

    protected SearchableListing(Collection<T> elements) {
        if (elements == null) {
            throw new IllegalArgumentException("Input elements should not be <null>");
        }
        this.elements = new ArrayList<T>(elements);
        this.searchCriteria = new ArrayList<SearchCriterion<T>>();
    }

    public void addSearchCriterion(SearchCriterion<T> searchCriterion) {
        if (searchCriterion == null) {
            throw new IllegalArgumentException("Search criterion should not be <null>");
        }
        searchCriteria.add(searchCriterion);
    }

    public void removeAllFilters() {
        searchCriteria.clear();
    }

    public void forEach(OnEachHandler<T> onEachHandler) {
        for (T each : elements) {
            onEachHandler.onEach(each, meetCriteria(each));
        }
    }

    protected boolean meetCriteria(T element) {
        for (SearchCriterion<T> eachSearchCriterion : searchCriteria) {
            if (!eachSearchCriterion.meetCriterion(element)) {
                return false;
            }
        }
        return true;
    }

    public List<T> toList() {
        List<T> list = new ArrayList<T>();
        for (T each : elements) {
            if (meetCriteria(each)) {
                list.add(each);
            }
        }
        return list;
    }

    private static class SearchResultImpl<T> implements SearchResult<T>, Serializable {
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
    }

    public List<SearchResult<T>> toListOfSearchResult() {
        final List<SearchResult<T>> listOfSearchResult = new ArrayList<SearchResult<T>>(elements.size());
        forEach(new OnEachHandler<T>() {
            @Override
            public void onEach(T each, boolean meetsCriteria) {
                listOfSearchResult.add(new SearchResultImpl<T>(each, meetsCriteria));
            }
        });
        return listOfSearchResult;
    }
}

