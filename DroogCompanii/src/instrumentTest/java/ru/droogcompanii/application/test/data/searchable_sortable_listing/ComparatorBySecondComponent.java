package ru.droogcompanii.application.test.data.searchable_sortable_listing;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by ls on 21.01.14.
 */
class ComparatorBySecondComponent implements Comparator<PairOfNumbers>, Serializable {
    @Override
    public int compare(PairOfNumbers pair1, PairOfNumbers pair2) {
        return pair1.second - pair2.second;
    }
}
