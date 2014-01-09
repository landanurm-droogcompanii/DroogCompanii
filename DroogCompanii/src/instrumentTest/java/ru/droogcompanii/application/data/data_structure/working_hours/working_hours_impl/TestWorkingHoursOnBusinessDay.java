package ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl;

import junit.framework.TestCase;

import ru.droogcompanii.application.data.data_structure.working_hours.IteratorOverTimes;
import ru.droogcompanii.application.data.data_structure.working_hours.Time;

/**
 * Created by ls on 09.01.14.
 */
public class TestWorkingHoursOnBusinessDay extends TestCase {

    private Time from;
    private Time to;
    private WorkingHoursOnBusinessDay workingHours;
    private WorkingHoursOnBusinessDay copy;


    @Override
    public void setUp() throws Exception {
        super.setUp();

        from = new Time(10, 0);
        to = new Time(19, 0);
        workingHours = new WorkingHoursOnBusinessDay(from, to);
        copy = new WorkingHoursOnBusinessDay(from, to);
    }


    public void testIncludes() {
        IteratorOverTimes.iterateAllTimesIn24Hours(new IteratorOverTimes.OnEachHandler() {
            @Override
            public void onEach(Time time) {
                assertEquals(time.within(from, to), workingHours.includes(time));
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
        int differentHours = (from.hours + 1) % 24;
        Time differentFrom = new Time(differentHours, from.minutes);
        assertFalse(workingHours.equals(new WorkingHoursOnBusinessDay(differentFrom, to)));
    }

    public void testNotEqualsWithDifferent_To() {
        int differentHours = Math.max((to.hours - 1), 0);
        Time differentTo = new Time(differentHours, to.minutes);
        assertFalse(workingHours.equals(new WorkingHoursOnBusinessDay(from, differentTo)));
    }

    public void testNotEqualsWithOtherTypeObject() {
        assertFalse(workingHours.equals("String"));
    }

    public void testHashCode() {
        assertEquals(workingHours.hashCode(), copy.hashCode());
    }
}
