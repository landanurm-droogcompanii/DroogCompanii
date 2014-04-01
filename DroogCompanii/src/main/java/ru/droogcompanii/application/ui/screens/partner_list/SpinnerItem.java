package ru.droogcompanii.application.ui.screens.partner_list;

import java.util.Comparator;

/**
 * Created by ls on 17.02.14.
 */
public class SpinnerItem<T> {
    private final SpinnerAdapterImpl.ComparatorProvider<T> comparatorProvider;
    private final int titleId;

    public SpinnerItem(int titleId, SpinnerAdapterImpl.ComparatorProvider<T> comparatorProvider) {
        this.titleId = titleId;
        this.comparatorProvider = comparatorProvider;
    }

    public int getTitleId() {
        return titleId;
    }

    public Comparator<T> getComparator() {
        return comparatorProvider.get();
    }
}
