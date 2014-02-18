package ru.droogcompanii.application.data.searchable_sortable_listing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ls on 17.02.14.
 */
public class CombinedSearchCriterion<T> implements SearchCriterion<T>, Serializable {
    private final List<SearchCriterion<T>> searchCriteria;

    public static <E> CombinedSearchCriterion<E> from(List<SearchCriterion<E>> searchCriteria) {
        return new CombinedSearchCriterion<E>(searchCriteria);
    }

    public CombinedSearchCriterion() {
        this.searchCriteria = new ArrayList<SearchCriterion<T>>();
    }

    public CombinedSearchCriterion(List<SearchCriterion<T>> searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public void add(SearchCriterion<T> criterion) {
        searchCriteria.add(criterion);
    }

    public void clear() {
        searchCriteria.clear();
    }

    @Override
    public boolean meetCriterion(T element) {
        for (SearchCriterion<T> each : searchCriteria) {
            if (!each.meetCriterion(element)) {
                return false;
            }
        }
        return true;
    }
}
