package ru.droogcompanii.application.test.data.searchable_sortable_listing;

import java.io.Serializable;

import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;

/**
 * Created by ls on 21.01.14.
 */
class OnlyEvenNumbersSearchCriterion implements SearchCriterion<Integer>, Serializable {
    @Override
    public boolean meetCriterion(Integer number) {
        return number % 2 == 0;
    }
}
