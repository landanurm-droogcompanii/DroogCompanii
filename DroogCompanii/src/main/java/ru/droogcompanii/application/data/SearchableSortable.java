package ru.droogcompanii.application.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ls on 14.01.14.
 */
public class SearchableSortable<T> {

    public static interface OnEachHandler<T> {
        void onEach(T each, boolean meetsCriteria);
    }

    public static interface SearchFilter<T> {
        boolean meetCriteria(T obj);
    }

    private final List<T> elements;
    private final List<SearchFilter<T>> searchFilters;
    private final List<Comparator<T>> comparators;


    public static <T> SearchableSortable<T> newInstance(Collection<T> elements) {
        return new SearchableSortable<T>(elements);
    }

    private SearchableSortable(Collection<T> elements) {
        this.elements = new ArrayList<T>(elements);
        this.searchFilters = new ArrayList<SearchFilter<T>>();
        this.comparators = new ArrayList<Comparator<T>>();
    }

    public void addSearchFilter(SearchFilter<T> searchFilter) {
        searchFilters.add(searchFilter);
    }

    public void addComparator(Comparator<T> comparator) {
        comparators.add(comparator);
    }

    public void forEach(OnEachHandler<T> onEachHandler) {
        for (T each : elements) {
            onEachHandler.onEach(each, meetCriteria(each));
        }
    }

    private boolean meetCriteria(T element) {
        for (SearchFilter<T> searchFilter : searchFilters) {
            if (!searchFilter.meetCriteria(element)) {
                return false;
            }
        }
        return true;
    }

    // Должна использоваться устойчивая сортировка.
    // Сортировки применяются в порядке
    // от "менее приоритетной" (с меньшим индексом)
    // к "более приоритетной" (с большим индексом).
    // В Java версиях 6 и 7 метод Collections.sort(List<T>, Comparator<T>)
    // гарантированно является устойчивым.
    public void sort() {
        sort(elements);
    }

    private void sort(List<T> list) {
        for (Comparator<T> comparator : comparators) {
            Collections.sort(list, comparator);
        }
    }

    public List<T> toList() {
        List<T> list = new ArrayList<T>();
        for (T each : elements) {
            if (meetCriteria(each)) {
                list.add(each);
            }
        }
        sort(list);
        return list;
    }
}
