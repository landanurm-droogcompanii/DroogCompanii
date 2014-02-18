package ru.droogcompanii.application.data.searchable_sortable_listing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ls on 14.01.14.
 */
public class SearchableSortableListing<T> extends SearchableListing<T> implements Serializable {

    private final CombinedComparator<T> combinedComparator;

    public static <T> SearchableSortableListing<T> newInstance(Collection<T> elements) {
        return new SearchableSortableListing<T>(elements);
    }

    protected SearchableSortableListing(Collection<T> elements) {
        super(elements);
        combinedComparator = new CombinedComparator<T>();
    }

    public void addComparator(Comparator<T> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator should not be <null>");
        }
        combinedComparator.add(comparator);
    }

    public void removeAllFilters() {
        super.removeAllFilters();
        combinedComparator.clear();
    }

    public void forEach(OnEachHandler<T> onEachHandler) {
        List<T> sortedElements = sorted(new ArrayList<T>(super.elements));
        for (T each : sortedElements) {
            onEachHandler.onEach(each, combinedSearchCriterion.meetCriterion(each));
        }
    }

    /*
        Should be used a stable sort.
        Sorting uses comparators in order: from first added to last added comparator.
        In Java 6 and 7 versions the method Collection.sort() is guaranteed to be stable.
    */
    private List<T> sorted(List<T> list) {
        if (noComparators()) {
            return list;
        }
        Collections.sort(list, combinedComparator);
        return list;
    }

    private boolean noComparators() {
        return combinedComparator.isEmpty();
    }

    public List<T> toList() {
        List<T> list = super.toList();
        return sorted(list);
    }

    @Override
    public List<SearchResult<T>> toListOfSearchResults() {
        return sortedSearchResults(super.toListOfSearchResults());
    }

    private List<SearchResult<T>> sortedSearchResults(List<SearchResult<T>> searchResults) {
        if (noComparators()) {
            return searchResults;
        }
        Comparator<SearchResult<T>> combinedSearchResultComparator = new Comparator<SearchResult<T>>() {
            @Override
            public int compare(SearchResult<T> e1, SearchResult<T> e2) {
                return combinedComparator.compare(e1.value(), e2.value());
            }
        };
        Collections.sort(searchResults, combinedSearchResultComparator);
        return searchResults;
    }
}
