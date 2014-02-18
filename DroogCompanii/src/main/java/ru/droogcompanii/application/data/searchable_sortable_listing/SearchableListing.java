package ru.droogcompanii.application.data.searchable_sortable_listing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ls on 21.01.14.
 */
public class SearchableListing<T> implements Serializable {

    protected final List<T> elements;
    protected final CombinedSearchCriterion<T> combinedSearchCriterion;


    public static <E> SearchableListing<E> newInstance(Collection<E> elements) {
        return new SearchableListing<E>(elements);
    }

    protected SearchableListing(Collection<T> elements) {
        if (elements == null) {
            throw new IllegalArgumentException("Input elements should not be <null>");
        }
        this.elements = new ArrayList<T>(elements);
        combinedSearchCriterion = new CombinedSearchCriterion<T>();
    }

    public void addSearchCriterion(SearchCriterion<T> searchCriterion) {
        if (searchCriterion == null) {
            throw new IllegalArgumentException("Search criterion should not be <null>");
        }
        combinedSearchCriterion.add(searchCriterion);
    }

    public void removeAllFilters() {
        combinedSearchCriterion.clear();
    }

    public void forEach(OnEachHandler<T> onEachHandler) {
        for (T each : elements) {
            onEachHandler.onEach(each, combinedSearchCriterion.meetCriterion(each));
        }
    }

    public List<T> toList() {
        List<T> list = new ArrayList<T>();
        for (T each : elements) {
            if (combinedSearchCriterion.meetCriterion(each)) {
                list.add(each);
            }
        }
        return list;
    }

    public List<SearchResult<T>> toListOfSearchResults() {
        final List<SearchResult<T>> searchResults = new ArrayList<SearchResult<T>>(elements.size());
        forEach(new OnEachHandler<T>() {
            @Override
            public void onEach(T each, boolean meetsCriteria) {
                searchResults.add(new SearchResultImpl<T>(each, meetsCriteria));
            }
        });
        return searchResults;
    }
}

