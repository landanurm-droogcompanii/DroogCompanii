package ru.droogcompanii.application.data.data_structure.working_hours.time;

import junit.framework.TestCase;

import ru.droogcompanii.application.UtilsForTest;
import ru.droogcompanii.application.data.data_structure.working_hours.IteratorOverTimes;
import ru.droogcompanii.application.util.SerializationUtils;

/**
 * Created by ls on 16.01.14.
 */
public class TestTimeRange extends TestCase {

    private Time from;
    private Time to;
    private TimeRange timeRange;
    private TimeRange copy;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        from = new Time(10, 20);
        to = new Time(20, 40);
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
            public void onEach(Time time) {
                assertEquals(time.within(from, to), timeRange.includes(time));
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
        int differentHours = (from.hours + 1) % 24;
        Time differentFrom = new Time(differentHours, from.minutes);
        assertFalse(timeRange.equals(new TimeRange(differentFrom, to)));
    }

    public void testNotEqualsWithDifferent_To() {
        int differentHours = Math.max((to.hours - 1), 0);
        Time differentTo = new Time(differentHours, to.minutes);
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
}
