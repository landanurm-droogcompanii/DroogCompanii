package ru.droogcompanii.application.data;

import android.util.Pair;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ls on 14.01.14.
 */
public class TestSearchableSortable extends TestCase {
    private final Integer[] array = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    private final Collection<Integer> collection = Arrays.asList(array);

    private SearchableSortable<Integer> searchableSortable;
    private int index;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        searchableSortable = SearchableSortable.newInstance(collection);
    }


    public void testWithoutSearchFilterAndComparator() {
        checkForEach(new SearchableSortable.OnEachHandler<Integer>() {
            @Override
            public void onEach(Integer each, boolean meetsCriteria) {
                assertEquals(array[index], each);
                assertTrue(meetsCriteria);
            }
        });
    }


    public void testWithSearchFilter() {
        searchableSortable.addSearchFilter(new OnlyEvenNumbersSearchFilter());
        checkForEach(new SearchableSortable.OnEachHandler<Integer>() {
            @Override
            public void onEach(Integer each, boolean meetsCriteria) {
                assertEquals(array[index], each);
                assertEquals(evenNumber(each), meetsCriteria);
            }
        });
    }

    private static class OnlyEvenNumbersSearchFilter implements SearchableSortable.SearchFilter<Integer> {
        @Override
        public boolean meetCriteria(Integer number) {
            return evenNumber(number);
        }
    }

    private static boolean evenNumber(Integer number) {
        return number % 2 == 0;
    }

    private void checkForEach(final SearchableSortable.OnEachHandler<Integer> onEachHandler) {
        index = 0;
        searchableSortable.forEach(new SearchableSortable.OnEachHandler<Integer>() {
            @Override
            public void onEach(Integer each, boolean meetsCriteria) {
                onEachHandler.onEach(each, meetsCriteria);
                ++index;
            }
        });
        assertEquals(array.length, index);
    }

    public void testWithSeveralSearchFilters() {
        searchableSortable.addSearchFilter(new OnlyEvenNumbersSearchFilter());
        final Integer[] numbers = { 1, 3 };
        searchableSortable.addSearchFilter(new OnlyTheseNumbersSearchFilter(numbers));
        checkForEach(new SearchableSortable.OnEachHandler<Integer>() {
            @Override
            public void onEach(Integer each, boolean meetsCriteria) {
                assertEquals(array[index], each);
                boolean expectedMeetsCriteria = (evenNumber(each) && arrayContainsElement(numbers, each));
                assertEquals(expectedMeetsCriteria, meetsCriteria);
            }
        });
    }

    private static class OnlyTheseNumbersSearchFilter implements SearchableSortable.SearchFilter<Integer> {
        private final Integer[] numbers;

        OnlyTheseNumbersSearchFilter(Integer... numbers) {
            this.numbers = numbers;
        }

        @Override
        public boolean meetCriteria(Integer element) {
            return arrayContainsElement(numbers, element);
        }
    }

    private static <T> boolean arrayContainsElement(T[] arr, T element) {
        for (T each : arr) {
            if (each.equals(element)) {
                return true;
            }
        }
        return false;
    }


    public void testWithComparator() {
        searchableSortable.addComparator(new ReverseOrderComparator());
        searchableSortable.sort();
        checkForEach(new SearchableSortable.OnEachHandler<Integer>() {
            @Override
            public void onEach(Integer each, boolean meetsCriteria) {
                assertEquals(getElementFromReversedArray(index), each);
                assertTrue(meetsCriteria);
            }
        });
    }

    private static class ReverseOrderComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer number1, Integer number2) {
            return -(number1 - number2);
        }
    }

    private Integer getElementFromReversedArray(int index) {
        int lastIndex = array.length - 1;
        return array[lastIndex - index];
    }

    private static class PairOfNumbers extends Pair<Integer, Integer> {
        public PairOfNumbers(Integer first, Integer second) {
            super(first, second);
        }
    }

    public void testWithSeveralComparators() {
        final List<PairOfNumbers> pairs = Arrays.asList(
                new PairOfNumbers(2, 4),
                new PairOfNumbers(6, 3),
                new PairOfNumbers(1, 4)
        );
        final List<PairOfNumbers> expectedPairsAfterSorting = Arrays.asList(
                new PairOfNumbers(1, 4),
                new PairOfNumbers(2, 4),
                new PairOfNumbers(6, 3)
        );
        SearchableSortable<PairOfNumbers> searchableSortablePairs = SearchableSortable.newInstance(pairs);
        searchableSortablePairs.addComparator(createComparatorBySecondComponent());
        searchableSortablePairs.addComparator(createComparatorByFirstComponent());
        index = 0;
        searchableSortablePairs.sort();
        searchableSortablePairs.forEach(new SearchableSortable.OnEachHandler<PairOfNumbers>() {
            @Override
            public void onEach(PairOfNumbers each, boolean meetsCriteria) {
                assertEquals(expectedPairsAfterSorting.get(index), each);
                assertTrue(meetsCriteria);
                ++index;
            }
        });
        assertEquals(expectedPairsAfterSorting.size(), index);
    }

    private Comparator<PairOfNumbers> createComparatorBySecondComponent() {
        return new Comparator<PairOfNumbers>() {
            @Override
            public int compare(PairOfNumbers pair1, PairOfNumbers pair2) {
                return pair1.second - pair2.second;
            }
        };
    }

    private Comparator<PairOfNumbers> createComparatorByFirstComponent() {
        return new Comparator<PairOfNumbers>() {
            @Override
            public int compare(PairOfNumbers pair1, PairOfNumbers pair2) {
                return pair1.first - pair2.first;
            }
        };
    }

}
