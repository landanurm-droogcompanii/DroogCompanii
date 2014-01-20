package ru.droogcompanii.application.test.util;

import junit.framework.TestCase;

import ru.droogcompanii.application.test.TestingUtils;
import ru.droogcompanii.application.util.MinMax;

/**
 * Created by ls on 17.01.14.
 */
public class TestMinMax extends TestCase {

    private MinMax<Integer> minMax;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        minMax = new MinMax<Integer>();
    }

    public void testMinMax() {
        final Integer min = 2;
        final Integer max = 123;
        final Integer middle = (min + max) / 2;
        minMax.add(middle);
        minMax.add(min);
        minMax.add(max);
        assertEquals(min, minMax.min());
        assertEquals(max, minMax.max());
    }

    public void testNotInitializedReturnsNullInsteadOfMinAndMax() {
        assertNull(minMax.min());
        assertNull(minMax.max());
    }

    public void testInitializedByOneValueReturnsThisOneValue() {
        final Integer value = 2;
        minMax.add(value);
        assertEquals(value, minMax.min());
        assertEquals(value, minMax.max());
    }

    public void testAdditionNullCausesException() {
        TestingUtils.assertExpectedException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                minMax.add(null);
            }
        });
    }
}
