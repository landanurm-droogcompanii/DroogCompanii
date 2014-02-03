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

    private final List<Comparator<T>> comparators;

    public static <T> SearchableSortableListing<T> newInstance(Collection<T> elements) {
        return new SearchableSortableListing<T>(elements);
    }

    protected SearchableSortableListing(Collection<T> elements) {
        super(elements);
        comparators = new ArrayList<Comparator<T>>();
    }

    public void addComparator(Comparator<T> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator should not be <null>");
        }
        comparators.add(comparator);
    }

    public void removeAllFilters() {
        super.removeAllFilters();
        comparators.clear();
    }

    public void forEach(OnEachHandler<T> onEachHandler) {
        for (T each : toList()) {
            onEachHandler.onEach(each, meetCriteria(each));
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
        Comparator<T> combinedComparator = new Comparator<T>() {
            @Override
            public int compare(T e1, T e2) {
                for (int i = comparators.size() - 1; i >= 0; --i) {
                    Comparator<T> comparator = comparators.get(i);
                    int result = comparator.compare(e1, e2);
                    if (result != 0) {
                        return result;
                    }
                }
                return 0;
            }
        };
        Collections.sort(list, combinedComparator);
        return list;
    }

    private boolean noComparators() {
        return comparators.isEmpty();
    }

    public List<T> toList() {
        List<T> list = super.toList();
        return sorted(list);
    }
}
