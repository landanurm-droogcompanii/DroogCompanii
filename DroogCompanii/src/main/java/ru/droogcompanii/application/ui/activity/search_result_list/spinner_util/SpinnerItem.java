package ru.droogcompanii.application.ui.activity.search_result_list.spinner_util;

import java.util.Comparator;

/**
 * Created by ls on 17.02.14.
 */
public class SpinnerItem<T> {
    private final Comparator<T> comparator;
    private final int titleId;

    public SpinnerItem(int titleId, Comparator<T> comparator) {
        this.titleId = titleId;
        this.comparator = comparator;
    }

    public int getTitleId() {
        return titleId;
    }

    public Comparator<T> getComparator() {
        return comparator;
    }
}
