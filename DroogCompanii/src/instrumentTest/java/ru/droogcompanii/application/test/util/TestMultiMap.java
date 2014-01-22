package ru.droogcompanii.application.test.util;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ru.droogcompanii.application.util.MultiMap;

/**
 * Created by ls on 22.01.14.
 */
public class TestMultiMap extends TestCase {

    private MultiMap<Integer, String> multiMap;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        multiMap = new MultiMap<Integer, String>();
    }

    public void testEmptyMultiMapReturnsEmptySetForAnyKey() {
        assertTrue(multiMap.get(13).isEmpty());
    }

    public void testGetReturnsEmptySetForNullKey() {
        assertTrue(multiMap.get(null).isEmpty());
    }

    public void testGetReturnsEmptySetForNonexistentKey() {
        multiMap.put(1, "Value 1");
        multiMap.put(2, "Value 2-1");
        multiMap.put(2, "Value 2-2");
        assertTrue(multiMap.get(3).isEmpty());
    }

    public void testGet() {
        multiMap.put(1, "1-1");
        multiMap.put(1, "1-2");
        multiMap.put(2, "2");
        Set<String> expectedValues = new HashSet<String>(Arrays.asList("1-1", "1-2"));
        assertEquals(expectedValues, multiMap.get(1));
    }

    public void testEmptyMultiMapReturnsEmptyKeySet() {
        assertTrue(multiMap.keySet().isEmpty());
    }

    public void testKeySet() {
        multiMap.put(1, "1-1");
        multiMap.put(1, "1-2");
        multiMap.put(2, "2");
        multiMap.put(3, "3");
        Set<Integer> expectedKeys = new HashSet<Integer>(Arrays.asList(1, 2, 3));
        assertEquals(expectedKeys, multiMap.keySet());
    }

    public void testPutAll() {
        Set<String> values = new HashSet<String>(Arrays.asList("1", "2", "3"));
        multiMap.putAll(1, values);
        assertEquals(values, multiMap.get(1));
    }

    public void testPutAllDoesNotReplaceExistingValues() {
        Set<String> values1 = new HashSet<String>(Arrays.asList("1", "2", "3"));
        multiMap.putAll(1, values1);
        Set<String> values2 = new HashSet<String>(Arrays.asList("4", "5", "6"));
        multiMap.putAll(1, values2);
        Set<String> expectedValues = new HashSet<String>(values1);
        expectedValues.addAll(values2);
        assertEquals(expectedValues, multiMap.get(1));
    }
}
