package ru.droogcompanii.application.test.data.working_hours.working_hours_impl;

import junit.framework.TestCase;

import ru.droogcompanii.application.data.time.TimeOfDay;
import ru.droogcompanii.application.data.time.TimeRange;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.WorkingHoursOnBusinessDayNewGen;
import ru.droogcompanii.application.util.IteratorOverTimes;
import ru.droogcompanii.application.data.working_hours.WorkingHours;

/**
 * Created by ls on 17.01.14.
 */
public class TestWorkingHoursOnBusinessDayNewGen extends TestCase {

    /*
        included range(s):
            10:00-21:00

        excluded range(s):
            12:00-13:00
            17:00-18:00

        if in xml specified a range [10:00-21:00]
        then:
            10:00 - within range
            20:59 - within range
            21:00 - not within range
    */

    private static final TimeRange[] includedRanges = {
        new TimeRange(new TimeOfDay(10, 0), new TimeOfDay(20, 59))
    };
    private static final TimeRange[] excludedRanges = {
            new TimeRange(new TimeOfDay(12, 0), new TimeOfDay(12, 59)),
            new TimeRange(new TimeOfDay(17, 0), new TimeOfDay(17, 59))
    };

    private WorkingHoursOnBusinessDayNewGen workingHours;
    private WorkingHoursOnBusinessDayNewGen copy;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        workingHours = prepareTestedInstance();
        copy = prepareTestedInstance();
    }

    private WorkingHoursOnBusinessDayNewGen prepareTestedInstance() {
        WorkingHoursOnBusinessDayNewGen testedInstance = new WorkingHoursOnBusinessDayNewGen();
        for (TimeRange timeRange : includedRanges) {
            testedInstance.include(timeRange);
        }
        for (TimeRange timeRange : excludedRanges) {
            testedInstance.exclude(timeRange);
        }
        return testedInstance;
    }

    public void test() {
        assertTrue(workingHours instanceof WorkingHours);
        IteratorOverTimes.iterateAllTimesIn24Hours(new IteratorOverTimes.OnEachHandler() {
            @Override
            public void onEach(TimeOfDay time) {
                assertEquals(isBusinessTime(time), workingHours.includes(time));
            }
        });
    }

    private boolean isBusinessTime(TimeOfDay time) {
        for (TimeRange range : excludedRanges) {
            if (range.includes(time)) {
                return false;
            }
        }
        for (TimeRange range : includedRanges) {
            if (range.includes(time)) {
                return true;
            }
        }
        return false;
    }

    public void testToString() {
        String workingHoursString = workingHours.toString();
        assertNotNull(workingHoursString);
        assertFalse(workingHoursString.trim().isEmpty());
    }

    public void testSetText() {
        String text = "Text of WorkingHoursOnBusinessDayNewGen";
        workingHours.setText(text);
        assertEquals(text, workingHours.toString());
    }

    public void testEqualsToItself() {
        assertEquals(workingHours, workingHours);
    }

    public void testEqualsToCopy() {
        assertEquals(workingHours, copy);
    }

    public void testNotEqualsToNull() {
        assertFalse(workingHours.equals(null));
    }

    public void testNotEqualsWithOtherTypeObject() {
        assertFalse(workingHours.equals("String"));
    }

    public void testHashCode() {
        assertEquals(workingHours.hashCode(), copy.hashCode());
    }
}
