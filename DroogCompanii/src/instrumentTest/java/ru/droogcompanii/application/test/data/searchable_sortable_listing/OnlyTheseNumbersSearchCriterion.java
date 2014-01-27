package ru.droogcompanii.application.test.data.searchable_sortable_listing;

import java.io.Serializable;

import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;

/**
 * Created by ls on 21.01.14.
 */
class OnlyTheseNumbersSearchCriterion implements SearchCriterion<Integer>, Serializable {
    private final Integer[] numbers;

    OnlyTheseNumbersSearchCriterion(Integer... numbers) {
        this.numbers = numbers;
    }

    @Override
    public boolean meetCriterion(Integer element) {
        return arrayContainsElement(numbers, element);
    }

    private static <T> boolean arrayContainsElement(T[] arr, T element) {
        for (T each : arr) {
            if (each.equals(element)) {
                return true;
            }
        }
        return false;
    }
}