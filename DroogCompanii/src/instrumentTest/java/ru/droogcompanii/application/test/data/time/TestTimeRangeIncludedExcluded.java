package ru.droogcompanii.application.test.data.time;

import junit.framework.TestCase;

import ru.droogcompanii.application.data.time.TimeOfDay;
import ru.droogcompanii.application.data.time.TimeRange;
import ru.droogcompanii.application.data.time.TimeRangeIncludedExcluded;
import ru.droogcompanii.application.test.TestingUtils;
import ru.droogcompanii.application.util.IteratorOverTimes;

/**
 * Created by ls on 16.01.14.
 */
public class TestTimeRangeIncludedExcluded extends TestCase {

    private TimeOfDay from;
    private TimeOfDay to;
    private TimeRangeIncludedExcluded timeRange;
    private TimeRangeIncludedExcluded copy;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        from = new TimeOfDay(10, 20);
        to = new TimeOfDay(20, 40);
        timeRange = new TimeRangeIncludedExcluded(from, to);
        copy = new TimeRangeIncludedExcluded(from, to);
    }

    public void testIsSerializable() {
        assertEquals(timeRange, TestingUtils.serializeAndDeserialize(timeRange));
    }

    public void testConstructor() {
        assertEquals(from, timeRange.from());
        assertEquals(to, timeRange.to());
    }

    public void testConstructorThrowsExceptionIfFromTimeIsBeforeToTime() {
        TestingUtils.assertExpectedException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                new TimeRangeIncludedExcluded(to, from);
            }
        });
    }

    public void testIncludes() {
        IteratorOverTimes.iterateAllTimesIn24Hours(new IteratorOverTimes.OnEachHandler() {
            @Override
            public void onEach(TimeOfDay time) {
                boolean expectedIncludes = !from.after(time) && to.after(time);
                assertEquals(expectedIncludes, timeRange.includes(time));
            }
        });
    }

    public void testEquals() {
        assertEquals(timeRange, copy);
    }

    public void testEqualsToItself() {
        assertEquals(timeRange, timeRange);
    }

    public void testNotEqualsToNull() {
        assertFalse(timeRange.equals(null));
    }

    public void testNotEqualsWithDifferent_From() {
        int differentHours = (from.getHours() + 1) % 24;
        TimeOfDay differentFrom = new TimeOfDay(differentHours, from.getMinutes());
        assertFalse(timeRange.equals(new TimeRangeIncludedExcluded(differentFrom, to)));
    }

    public void testNotEqualsWithDifferent_To() {
        int differentHours = Math.max((to.getHours() - 1), 0);
        TimeOfDay differentTo = new TimeOfDay(differentHours, to.getMinutes());
        assertFalse(timeRange.equals(new TimeRangeIncludedExcluded(from, differentTo)));
    }

    public void testNotEqualsWithOtherTypeObject() {
        assertFalse(timeRange.equals("String"));
    }

    public void testHashCode() {
        assertEquals(timeRange.hashCode(), copy.hashCode());
    }

    public void testToString() {
        String expected = from + TimeRange.SEPARATOR + to;
        assertEquals(expected, timeRange.toString());
    }
}
