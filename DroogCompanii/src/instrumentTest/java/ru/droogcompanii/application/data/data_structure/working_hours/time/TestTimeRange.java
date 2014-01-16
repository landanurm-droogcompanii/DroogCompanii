package ru.droogcompanii.application.data.data_structure.working_hours.time;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import java.io.Serializable;

import ru.droogcompanii.application.data.data_structure.working_hours.IteratorOverTimes;

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
        assertTrue(timeRange instanceof Serializable);
    }

    public void testConstructor() {
        assertEquals(from, timeRange.from());
        assertEquals(to, timeRange.to());
    }

    public void testConstructorThrowsExceptionIfFromTimeIsBeforeToTime() {
        try {
            timeRange = new TimeRange(to, from);

            throw new AssertionFailedError(TimeRange.class.getName() +
                " constructor should throw IllegalArgumentException, if <from> time is before <to> time"
            );
        } catch (IllegalArgumentException e) {
            // all right
        }
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
