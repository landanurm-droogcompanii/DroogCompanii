package ru.droogcompanii.application.data.searchable_sortable_listing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ls on 17.02.14.
 */
public class CombinedComparator<T> implements Comparator<T>, Serializable {

    private final List<Comparator<T>> comparators;

    public static <E> CombinedComparator from(List<Comparator<E>> comparators) {
        return new CombinedComparator<E>(comparators);
    }

    public CombinedComparator() {
        comparators = new ArrayList<Comparator<T>>();
    }

    public CombinedComparator(List<Comparator<T>> comparators) {
        this.comparators = comparators;
    }

    public void add(Comparator<T> comparator) {
        comparators.add(comparator);
    }

    public void clear() {
        comparators.clear();
    }

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

    public boolean isEmpty() {
        return comparators.isEmpty();
    }
}
