package ru.droogcompanii.application.test.data.searchable_sortable_listing;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ru.droogcompanii.application.data.searchable_sortable_listing.OnEachHandler;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;
import ru.droogcompanii.application.test.TestingUtils;

/**
 * Created by ls on 14.01.14.
 */
public class TestSearchableListing extends TestCase {
    private final Integer[] array = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

    private int index;
    private List<Integer> elements;
    private SearchableListing<Integer> searchableListing;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        elements = Arrays.asList(array);
        searchableListing = SearchableListing.newInstance(elements);
    }

    public void testClassUsesCopyOfInputCollection() {
        List<Integer> before = new ArrayList<Integer>(elements);
        searchableListing.addSearchCriterion(new OnlyEvenNumbersSearchCriterion());
        searchableListing.forEach(new OnEachHandler<Integer>() {
            @Override
            public void onEach(Integer each, boolean meetsCriteria) {
                // do nothing
            }
        });
        assertEquals(before, elements);
    }

    public void testForEachWithoutSearchCriterion() {
        checkForEach(new OnEachHandler<Integer>() {
            @Override
            public void onEach(Integer each, boolean meetsCriteria) {
                assertEquals(array[index], each);
                assertTrue(meetsCriteria);
            }
        });
    }

    public void testForEachWithSearchCriterion() {
        searchableListing.addSearchCriterion(new OnlyEvenNumbersSearchCriterion());
        checkForEach(new OnEachHandler<Integer>() {
            @Override
            public void onEach(Integer each, boolean meetsCriteria) {
                assertEquals(array[index], each);
                assertEquals(ListingTestingUtils.isEvenNumber(each), meetsCriteria);
            }
        });
    }

    private void checkForEach(final OnEachHandler<Integer> onEachHandler) {
        index = 0;
        searchableListing.forEach(new OnEachHandler<Integer>() {
            @Override
            public void onEach(Integer each, boolean meetsCriteria) {
                onEachHandler.onEach(each, meetsCriteria);
                ++index;
            }
        });
        assertEquals(array.length, index);
    }

    public void testForEachWithSeveralSearchCriteria() {
        final SearchCriterion<Integer> onlyEvenNumbersCriterion =
                                        new OnlyEvenNumbersSearchCriterion();
        final SearchCriterion<Integer> onlyTheseNumbersCriterion =
                                        new OnlyTheseNumbersSearchCriterion(2, 8, 120);
        searchableListing.addSearchCriterion(onlyEvenNumbersCriterion);
        searchableListing.addSearchCriterion(onlyTheseNumbersCriterion);
        checkForEach(new OnEachHandler<Integer>() {
            @Override
            public void onEach(Integer each, boolean meetsCriteria) {
                assertEquals(array[index], each);
                boolean expectedMeetsCriteria = ListingTestingUtils.meetCriteria(each,
                        onlyEvenNumbersCriterion, onlyTheseNumbersCriterion);
                assertEquals(expectedMeetsCriteria, meetsCriteria);
            }
        });
    }

    public void testToListWithoutSearchCriterion() {
        assertEquals(elements, searchableListing.toList());
    }

    public void testToListWithSearchCriterion() {
        SearchCriterion<Integer> searchCriterion = new OnlyEvenNumbersSearchCriterion();
        searchableListing.addSearchCriterion(searchCriterion);
        Collection<Integer> numbersWhichMeetCriteria =
                ListingTestingUtils.getNumbersWhichMeetCriteria(elements, searchCriterion);
        assertEquals(numbersWhichMeetCriteria, searchableListing.toList());
    }

    public void testToListWithSeveralSearchCriteria() {
        SearchCriterion<Integer>
                onlyEvenNumbersSearchCriterion = new OnlyEvenNumbersSearchCriterion();
        SearchCriterion<Integer>
                onlyTheseNumbersSearchCriterion = new OnlyTheseNumbersSearchCriterion(2, 4, 6);
        searchableListing.addSearchCriterion(onlyEvenNumbersSearchCriterion);
        searchableListing.addSearchCriterion(onlyTheseNumbersSearchCriterion);
        Collection<Integer> numbersWhichMeetCriteria =
                ListingTestingUtils.getNumbersWhichMeetCriteria(elements,
                        onlyEvenNumbersSearchCriterion, onlyTheseNumbersSearchCriterion);
        assertEquals(numbersWhichMeetCriteria, searchableListing.toList());
    }


    public void testRemoveAllFilters() {
        searchableListing.addSearchCriterion(new OnlyEvenNumbersSearchCriterion());

        searchableListing.removeAllFilters();

        checkForEach(new OnEachHandler<Integer>() {
            @Override
            public void onEach(Integer each, boolean meetsCriteria) {
                assertEquals(elements.get(index), each);
                assertTrue(meetsCriteria);
            }
        });

        assertEquals(elements, searchableListing.toList());
    }

    public void testIsSerializable() {
        SearchableListing<Integer> notSerialized = searchableListing;
        notSerialized.addSearchCriterion(new OnlyEvenNumbersSearchCriterion());
        notSerialized.addSearchCriterion(new OnlyTheseNumbersSearchCriterion(1, 2, 3, 4, 5));
        SearchableListing<Integer> serialized = TestingUtils.serializeAndDeserialize(notSerialized);
        assertEquals(notSerialized.toList(), serialized.toList());
    }

}
