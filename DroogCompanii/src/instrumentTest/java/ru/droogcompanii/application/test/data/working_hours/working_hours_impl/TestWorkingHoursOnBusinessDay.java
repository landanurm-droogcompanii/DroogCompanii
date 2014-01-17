package ru.droogcompanii.application.test.data.working_hours.working_hours_impl;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import ru.droogcompanii.application.data.time.TimeOfDay;
import ru.droogcompanii.application.data.time.TimeRange;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.WorkingHoursOnBusinessDay;
import ru.droogcompanii.application.util.IteratorOverTimes;

/**
 * Created by ls on 09.01.14.
 */
public class TestWorkingHoursOnBusinessDay extends TestCase {

    private TimeOfDay from;
    private TimeOfDay to;
    private WorkingHoursOnBusinessDay workingHours;
    private WorkingHoursOnBusinessDay copy;


    @Override
    public void setUp() throws Exception {
        super.setUp();

        from = new TimeOfDay(10, 0);
        to = new TimeOfDay(19, 0);
        workingHours = new WorkingHoursOnBusinessDay(from, to);
        copy = new WorkingHoursOnBusinessDay(from, to);
    }

    public void testConstructorThrowsExceptionIfFromTimeIsBeforeToTime() {
        try {
            new WorkingHoursOnBusinessDay(to, from);
            throw new AssertionFailedError(WorkingHoursOnBusinessDay.class.getName() +
                " constructor should throw IllegalArgumentException, if <from> time is before <to> time"
            );
        } catch (IllegalArgumentException e) {
            // all right
        }
    }

    public void testIncludes() {
        final TimeRange timeRange = new TimeRange(from, to);
        IteratorOverTimes.iterateAllTimesIn24Hours(new IteratorOverTimes.OnEachHandler() {
            @Override
            public void onEach(TimeOfDay time) {
                assertEquals(time.within(timeRange), workingHours.includes(time));
            }
        });
    }

    public void testToString() {
        String expected = from + WorkingHoursOnBusinessDay.SEPARATOR_BETWEEN_TIMES + to;
        assertEquals(expected, workingHours.toString());
    }

    public void testEquals() {
        assertEquals(workingHours, copy);
    }

    public void testEqualsToItself() {
        assertEquals(workingHours, workingHours);
    }

    public void testNotEqualsToNull() {
        assertFalse(workingHours.equals(null));
    }

    public void testNotEqualsWithDifferent_From() {
        int differentHours = (from.getHours() + 1) % 24;
        TimeOfDay differentFrom = new TimeOfDay(differentHours, from.getMinutes());
        assertFalse(workingHours.equals(new WorkingHoursOnBusinessDay(differentFrom, to)));
    }

    public void testNotEqualsWithDifferent_To() {
        int differentHours = Math.max((to.getHours() - 1), 0);
        TimeOfDay differentTo = new TimeOfDay(differentHours, to.getMinutes());
        assertFalse(workingHours.equals(new WorkingHoursOnBusinessDay(from, differentTo)));
    }

    public void testNotEqualsWithOtherTypeObject() {
        assertFalse(workingHours.equals("String"));
    }

    public void testHashCode() {
        assertEquals(workingHours.hashCode(), copy.hashCode());
    }
}
