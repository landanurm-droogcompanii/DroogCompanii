package ru.droogcompanii.application.test.data.time;

import junit.framework.TestCase;

import ru.droogcompanii.application.data.time.TimeOfDay;
import ru.droogcompanii.application.data.time.TimeRange;
import ru.droogcompanii.application.test.UtilsForTest;
import ru.droogcompanii.application.util.IteratorOverTimes;
import ru.droogcompanii.application.util.SerializationUtils;

/**
 * Created by ls on 16.01.14.
 */
public class TestTimeRange extends TestCase {

    private TimeOfDay from;
    private TimeOfDay to;
    private TimeRange timeRange;
    private TimeRange copy;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        from = new TimeOfDay(10, 20);
        to = new TimeOfDay(20, 40);
        timeRange = new TimeRange(from, to);
        copy = new TimeRange(from, to);
    }

    public void testTimeRangeIsSerializable() {
        byte[] bytes = SerializationUtils.serialize(timeRange);
        TimeRange deserialized = (TimeRange) SerializationUtils.deserialize(bytes);
        assertEquals(timeRange, deserialized);
    }

    public void testConstructor() {
        assertEquals(from, timeRange.from());
        assertEquals(to, timeRange.to());
    }

    public void testConstructorThrowsExceptionIfFromTimeIsBeforeToTime() {
        UtilsForTest.assertExpectedException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                new TimeRange(to, from);
            }
        });
    }

    public void testIncludes() {
        IteratorOverTimes.iterateAllTimesIn24Hours(new IteratorOverTimes.OnEachHandler() {
            @Override
            public void onEach(TimeOfDay time) {
                assertEquals(time.within(timeRange), timeRange.includes(time));
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
        assertFalse(timeRange.equals(new TimeRange(differentFrom, to)));
    }

    public void testNotEqualsWithDifferent_To() {
        int differentHours = Math.max((to.getHours() - 1), 0);
        TimeOfDay differentTo = new TimeOfDay(differentHours, to.getMinutes());
        assertFalse(timeRange.equals(new TimeRange(from, differentTo)));
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


    public void testNewIncludedExcluded() {
        final TimeRange includedExcluded = TimeRange.newIncludedExcluded(from, to);
        IteratorOverTimes.iterateAllTimesIn24Hours(new IteratorOverTimes.OnEachHandler() {
            @Override
            public void onEach(TimeOfDay time) {
                boolean expectedIncludes = !time.equals(to) && timeRange.includes(time);
                assertEquals(expectedIncludes, includedExcluded.includes(time));
            }
        });
    }


    public void testNewExcludedIncluded() {
        final TimeRange excludedIncluded = TimeRange.newExcludedIncluded(from, to);
        IteratorOverTimes.iterateAllTimesIn24Hours(new IteratorOverTimes.OnEachHandler() {
            @Override
            public void onEach(TimeOfDay time) {
                boolean expectedIncludes = !time.equals(from) && timeRange.includes(time);
                assertEquals(expectedIncludes, excludedIncluded.includes(time));
            }
        });
    }


    public void testNewExcludedExcluded() {
        final TimeRange excludedExcluded = TimeRange.newExcludedExcluded(from, to);
        IteratorOverTimes.iterateAllTimesIn24Hours(new IteratorOverTimes.OnEachHandler() {
            @Override
            public void onEach(TimeOfDay time) {
                boolean expectedIncludes = !time.equals(from) && !time.equals(to) && timeRange.includes(time);
                assertEquals(expectedIncludes, excludedExcluded.includes(time));
            }
        });
    }
}
