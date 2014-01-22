package ru.droogcompanii.application.test.data.working_hours.working_hours_impl;

import junit.framework.TestCase;

import ru.droogcompanii.application.data.time.TimeOfDay;
import ru.droogcompanii.application.data.working_hours.WorkingHours;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.DayAndNightWorkingHours;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.WorkingHoursOnHoliday;
import ru.droogcompanii.application.test.TestingUtils;
import ru.droogcompanii.application.util.IteratorOverTimes;

/**
 * Created by ls on 09.01.14.
 */
public class TestDayAndNightWorkingHours extends TestCase {

    private String message;
    private DayAndNightWorkingHours workingHours;
    private DayAndNightWorkingHours copy;

    @Override
    public void setUp() throws Exception {
        message = "this is message";
        workingHours = new DayAndNightWorkingHours(message);
        copy = new DayAndNightWorkingHours(message);
    }

    public void testToString() {
        assertEquals(message, workingHours.toString());
    }

    public void testIncludes() {
        IteratorOverTimes.iterateAllTimesIn24Hours(new IteratorOverTimes.OnEachHandler() {
            @Override
            public void onEach(TimeOfDay time) {
                assertTrue(workingHours.includes(time));
            }
        });
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

    public void testNotEqualsWithDifferentMessage() {
        assertFalse(workingHours.equals(new DayAndNightWorkingHours(message + "12345")));
    }

    public void testNotEqualsToWorkingHoursOnHoliday() {
        WorkingHours onHoliday = new WorkingHoursOnHoliday(message);
        assertFalse(workingHours.equals(onHoliday));
    }

    public void testNotEqualsToOtherTypeObject() {
        assertFalse(workingHours.equals(message));
    }

    public void testHashCode() {
        assertEquals(workingHours.hashCode(), copy.hashCode());
    }

    public void testIsSerializable() {
        assertEquals(workingHours, TestingUtils.serializeAndDeserialize(workingHours));
    }
}
