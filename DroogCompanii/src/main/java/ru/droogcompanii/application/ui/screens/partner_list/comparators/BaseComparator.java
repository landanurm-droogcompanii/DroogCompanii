package ru.droogcompanii.application.ui.screens.partner_list.comparators;

import java.util.Comparator;

/**
 * Created by ls on 02.04.14.
 */
abstract class BaseComparator<T> implements Comparator<T> {
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return getClass().equals(obj.getClass());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}