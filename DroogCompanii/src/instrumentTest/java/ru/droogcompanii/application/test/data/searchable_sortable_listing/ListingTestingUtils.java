package ru.droogcompanii.application.test.data.searchable_sortable_listing;

import java.util.ArrayList;
import java.util.Collection;

import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;

/**
 * Created by ls on 21.01.14.
 */
class ListingTestingUtils {

    public static Collection<Integer> getNumbersWhichMeetCriteria(Collection<Integer> numbers,
                           SearchCriterion<Integer>... searchCriteria) {
        Collection<Integer> numbersWhichMeetCriteria = new ArrayList<Integer>();
        for (Integer each : numbers) {
            if (meetCriteria(each, searchCriteria)) {
                numbersWhichMeetCriteria.add(each);
            }
        }
        return numbersWhichMeetCriteria;
    }

    public static boolean meetCriteria(Integer each, SearchCriterion<Integer>... searchCriteria) {
        for (SearchCriterion<Integer> criterion : searchCriteria) {
            if (!criterion.meetCriterion(each)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEvenNumber(int number) {
        return number % 2 == 0;
    }
}
