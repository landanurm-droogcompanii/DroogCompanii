package ru.droogcompanii.application.test.data.searchable_sortable_listing;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by ls on 21.01.14.
 */
class ReverseOrderComparator implements Comparator<Integer>, Serializable {
    @Override
    public int compare(Integer number1, Integer number2) {
        return -(number1 - number2);
    }
}
