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

    private final List<T> unsortedElements;
    private final List<Comparator<T>> comparators;
    private boolean sorted;

    public static <T> SearchableSortableListing<T> newInstance(Collection<T> elements) {
        return new SearchableSortableListing<T>(elements);
    }

    protected SearchableSortableListing(Collection<T> elements) {
        super(elements);
        unsortedElements = new ArrayList<T>(elements);
        comparators = new ArrayList<Comparator<T>>();
    }

    public void addComparator(Comparator<T> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator should not be <null>");
        }
        comparators.add(comparator);
        sorted = false;
    }

    public void removeAllFilters() {
        super.removeAllFilters();
        comparators.clear();
        elements = new ArrayList<T>(unsortedElements);
    }

    public void forEach(OnEachHandler<T> onEachHandler) {
        sortIfNeed();
        for (T each : elements) {
            onEachHandler.onEach(each, meetCriteria(each));
        }
    }

    private void sortIfNeed() {
        if (sorted || noComparators()) {
            return;
        }
        sort(elements);
        sorted = true;
    }

    private boolean noComparators() {
        return comparators.isEmpty();
    }

    /*
        Should be used a stable sort.
        Sorting uses comparators in order: from first added to last added comparator.
        In Java 6 and 7 versions the method Collection.sort() is guaranteed to be stable.
    */
    private void sort(List<T> list) {
        for (Comparator<T> comparator : comparators) {
            Collections.sort(list, comparator);
        }
    }

    public List<T> toList() {
        sortIfNeed();
        List<T> list = super.toList();
        return list;
    }
}
