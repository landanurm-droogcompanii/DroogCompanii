package ru.droogcompanii.application.test.util;

import junit.framework.TestCase;

import ru.droogcompanii.application.util.MinMax;

/**
 * Created by ls on 17.01.14.
 */
public class TestMinMax extends TestCase {

    public void testMinMax() {
        MinMax<Integer> minMax = new MinMax<Integer>();
        Integer min = 2;
        Integer max = 123;
        Integer middle = (min + max) / 2;
        minMax.add(middle);
        minMax.add(min);
        minMax.add(max);
        assertEquals(min, minMax.min());
        assertEquals(max, minMax.max());
    }

    public void testNotInitializedReturnsNullInsteadOfMinAndMax() {
        MinMax<Integer> minMax = new MinMax<Integer>();
        assertNull(minMax.min());
        assertNull(minMax.max());
    }

    public void testInitializedByOneValueReturnsThisOneValue() {
        MinMax<Integer> minMax = new MinMax<Integer>();
        Integer value = 2;
        minMax.add(value);
        assertEquals(value, minMax.min());
        assertEquals(value, minMax.max());
    }
}
