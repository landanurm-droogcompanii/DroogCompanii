package ru.droogcompanii.application.test.data.searchable_sortable_listing;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableSortableListing;
import ru.droogcompanii.application.test.TestingUtils;

/**
 * Created by ls on 14.01.14.
 */
public class TestSearchableSortableListing extends TestCase {
    private final Integer[] array = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

    private int index;
    private List<Integer> elements;
    private SearchableSortableListing<Integer> searchableSortableListing;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        elements = Arrays.asList(array);
        searchableSortableListing = SearchableSortableListing.newInstance(elements);
    }


    public void testClassUsesCopyOfInputCollection() {
        List<Integer> before = new ArrayList<Integer>(elements);
        searchableSortableListing.addComparator(new ReverseOrderComparator());
        searchableSortableListing.addSearchCriterion(new OnlyEvenNumbersSearchCriterion());
        searchableSortableListing.forEach(new SearchableSortableListing.OnEachHandler<Integer>() {
            @Override
            public void onEach(Integer each, boolean meetsCriteria) {
                // do nothing
            }
        });
        assertEquals(before, elements);
    }


    public void testForEachWithoutSearchCriterionAndComparator() {
        checkForEach(new SearchableSortableListing.OnEachHandler<Integer>() {
            @Override
            public void onEach(Integer each, boolean meetsCriteria) {
                assertEquals(array[index], each);
                assertTrue(meetsCriteria);
            }
        });
    }

    private void checkForEach(final SearchableSortableListing.OnEachHandler<Integer> onEachHandler) {
        index = 0;
        searchableSortableListing.forEach(new SearchableSortableListing.OnEachHandler<Integer>() {
            @Override
            public void onEach(Integer each, boolean meetsCriteria) {
                onEachHandler.onEach(each, meetsCriteria);
                ++index;
            }
        });
        assertEquals(array.length, index);
    }

    public void testForEachWithComparator() {
        searchableSortableListing.addComparator(new ReverseOrderComparator());
        checkForEach(new SearchableSortableListing.OnEachHandler<Integer>() {
            @Override
            public void onEach(Integer each, boolean meetsCriteria) {
                assertEquals(getElementFromReversedArray(index), each);
                assertTrue(meetsCriteria);
            }
        });
    }

    private Integer getElementFromReversedArray(int index) {
        int lastIndex = array.length - 1;
        return array[lastIndex - index];
    }

    public void testForEachWithSeveralComparators() {
        final List<PairOfNumbers> pairs = Arrays.asList(
                new PairOfNumbers(2, 4),
                new PairOfNumbers(6, 3),
                new PairOfNumbers(1, 4),
                new PairOfNumbers(2, 3)
        );
        final List<PairOfNumbers> expectedPairsAfterSorting = Arrays.asList(
                new PairOfNumbers(1, 4),
                new PairOfNumbers(2, 3),
                new PairOfNumbers(2, 4),
                new PairOfNumbers(6, 3)
        );
        SearchableSortableListing<PairOfNumbers> searchableSortablePairs =
                SearchableSortableListing.newInstance(pairs);
        searchableSortablePairs.addComparator(new ComparatorBySecondComponent());
        searchableSortablePairs.addComparator(new ComparatorByFirstComponent());
        index = 0;
        searchableSortablePairs.forEach(new SearchableSortableListing.OnEachHandler<PairOfNumbers>() {
            @Override
            public void onEach(PairOfNumbers each, boolean meetsCriteria) {
                assertEquals(expectedPairsAfterSorting.get(index), each);
                assertTrue(meetsCriteria);
                ++index;
            }
        });
        assertEquals(expectedPairsAfterSorting.size(), index);
    }


    public void testToListWithoutSearchCriterionAndComparator() {
        assertEquals(elements, searchableSortableListing.toList());
    }

    public void testToListWithComparator() {
        searchableSortableListing.addComparator(new ReverseOrderComparator());
        assertEquals(reversed(elements), searchableSortableListing.toList());
    }

    private List<Integer> reversed(List<Integer> list) {
        List<Integer> reversedList = new ArrayList(list.size());
        for (int i = list.size() - 1; i >= 0; --i) {
            reversedList.add(list.get(i));
        }
        return reversedList;
    }


    public void testToListWithSeveralComparators() {
        final List<PairOfNumbers> pairs = Arrays.asList(
                new PairOfNumbers(2, 4),
                new PairOfNumbers(6, 3),
                new PairOfNumbers(1, 4),
                new PairOfNumbers(2, 3)
        );
        final List<PairOfNumbers> expectedPairs = Arrays.asList(
                new PairOfNumbers(1, 4),
                new PairOfNumbers(2, 3),
                new PairOfNumbers(2, 4),
                new PairOfNumbers(6, 3)
        );
        SearchableSortableListing<PairOfNumbers> searchableSortablePairs = SearchableSortableListing.newInstance(pairs);
        searchableSortablePairs.addComparator(new ComparatorBySecondComponent());
        searchableSortablePairs.addComparator(new ComparatorByFirstComponent());

        assertEquals(expectedPairs, searchableSortablePairs.toList());
    }


    public void testClearAllCriteria() {
        final List<PairOfNumbers> pairs = Arrays.asList(
                new PairOfNumbers(2, 4),
                new PairOfNumbers(6, 3),
                new PairOfNumbers(1, 4),
                new PairOfNumbers(2, 3),
                new PairOfNumbers(1, 1),
                new PairOfNumbers(5, 5),
                new PairOfNumbers(2, 2)
        );
        SearchableSortableListing<PairOfNumbers> searchableSortablePairs = SearchableSortableListing.newInstance(pairs);
        searchableSortablePairs.addSearchCriterion(new SearchableSortableListing.SearchCriterion<PairOfNumbers>() {
            @Override
            public boolean meetCriterion(PairOfNumbers obj) {
                return obj.first != obj.second;
            }
        });
        searchableSortablePairs.addComparator(new ComparatorBySecondComponent());
        searchableSortablePairs.addComparator(new ComparatorByFirstComponent());

        searchableSortablePairs.forEach(new SearchableListing.OnEachHandler<PairOfNumbers>() {
            @Override
            public void onEach(PairOfNumbers each, boolean meetsCriteria) {
                // do nothing
            }
        });
        searchableSortablePairs.toList();

        searchableSortablePairs.clearAllCriteria();

        index = 0;
        searchableSortablePairs.forEach(new SearchableSortableListing.OnEachHandler<PairOfNumbers>() {
            @Override
            public void onEach(PairOfNumbers each, boolean meetsCriteria) {
                assertEquals(pairs.get(index), each);
                assertTrue(meetsCriteria);
                ++index;
            }
        });
        assertEquals(pairs.size(), index);

        assertEquals(pairs, searchableSortablePairs.toList());
    }


    public void testIsSerializable() {
        SearchableSortableListing<Integer> notSerialized = searchableSortableListing;
        notSerialized.addComparator(new ReverseOrderComparator());
        notSerialized.addSearchCriterion(new OnlyEvenNumbersSearchCriterion());
        notSerialized.addSearchCriterion(new OnlyTheseNumbersSearchCriterion(1, 2, 3, 4, 5));
        SearchableSortableListing<Integer> serialized = TestingUtils.serializeAndDeserialize(notSerialized);
        assertEquals(notSerialized.toList(), serialized.toList());
    }

}
