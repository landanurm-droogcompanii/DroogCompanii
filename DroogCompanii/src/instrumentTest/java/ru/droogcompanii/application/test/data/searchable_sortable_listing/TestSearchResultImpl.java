package ru.droogcompanii.application.test.data.searchable_sortable_listing;

import junit.framework.TestCase;

import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResult;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResultImpl;

/**
 * Created by ls on 06.02.14.
 */
public class TestSearchResultImpl extends TestCase {

    private boolean meetsCriteria;
    private Integer value;
    private SearchResult<Integer> searchResult;


    @Override
    public void setUp() throws Exception {
        super.setUp();

        value = 1;
        meetsCriteria = true;
        searchResult = new SearchResultImpl<Integer>(value, meetsCriteria);
    }

    public void testConstructor() {
        assertEquals(value, searchResult.value());
        assertEquals(meetsCriteria, searchResult.meetsSearchCriteria());
    }

    public void testEqualsToItself() {
        assertEquals(searchResult, searchResult);
    }

    public void testDoesNotEqualToNull() {
        assertFalse(searchResult.equals(null));
    }

    public void testEquals() {
        assertEquals(searchResult, copyOf(searchResult));
    }

    private static <T> SearchResult<T> copyOf(SearchResult<T> searchResult) {
        return new SearchResultImpl<T>(searchResult.value(), searchResult.meetsSearchCriteria());
    }

    public void testDoesNotEqualWithDifferent_Value() {
        assertSearchResultsAreNotEqual(1, true, 2, true);
    }

    private static <T> void assertSearchResultsAreNotEqual(T value1, boolean meets1, T value2, boolean meets2) {
        SearchResult<T> one = new SearchResultImpl<T>(value1, meets1);
        SearchResult<T> two = new SearchResultImpl<T>(value2, meets2);
        assertFalse(one.equals(two));
    }

    public void testDoesNotEqualWithDifferent_MeetsSearchCriterion() {
        assertSearchResultsAreNotEqual(1, true, 1, false);
    }

    public void testTwoEqualObjectsHaveTheSameHashCode() {
        assertEquals(searchResult.hashCode(), copyOf(searchResult).hashCode());
    }

}
