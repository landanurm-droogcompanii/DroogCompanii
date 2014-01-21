package ru.droogcompanii.application.test.data.working_hours.working_hours_impl;

import junit.framework.TestCase;

import java.util.Arrays;

import ru.droogcompanii.application.data.time.TimeOfDay;
import ru.droogcompanii.application.data.time.TimeRangeIncludedExcluded;
import ru.droogcompanii.application.data.working_hours.WorkingHours;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.WorkingHoursOnBusinessDay;
import ru.droogcompanii.application.test.TestingUtils;
import ru.droogcompanii.application.util.IteratorOverTimes;

/**
 * Created by ls on 17.01.14.
 */
public class TestWorkingHoursOnBusinessDay extends TestCase {

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

    private static final TimeRangeIncludedExcluded[] includedRanges = {
        new TimeRangeIncludedExcluded(new TimeOfDay(10, 0), new TimeOfDay(20, 59))
    };
    private static final TimeRangeIncludedExcluded[] excludedRanges = {
            new TimeRangeIncludedExcluded(new TimeOfDay(12, 0), new TimeOfDay(12, 59)),
            new TimeRangeIncludedExcluded(new TimeOfDay(17, 0), new TimeOfDay(17, 59))
    };

    private WorkingHoursOnBusinessDay workingHours;
    private WorkingHoursOnBusinessDay copy;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        workingHours = prepareTestedInstance();
        copy = prepareTestedInstance();
    }

    private WorkingHoursOnBusinessDay prepareTestedInstance() {
        WorkingHoursOnBusinessDay testedInstance = new WorkingHoursOnBusinessDay();
        for (TimeRangeIncludedExcluded timeRange : includedRanges) {
            testedInstance.include(timeRange);
        }
        for (TimeRangeIncludedExcluded timeRange : excludedRanges) {
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
        for (TimeRangeIncludedExcluded range : excludedRanges) {
            if (range.includes(time)) {
                return false;
            }
        }
        for (TimeRangeIncludedExcluded range : includedRanges) {
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

    public void testGetIncludedRanges() {
        assertEquals(Arrays.asList(includedRanges), workingHours.getIncludedRanges());
    }

    public void testGetExcludedRanges() {
        assertEquals(Arrays.asList(excludedRanges), workingHours.getExcludedRanges());
    }

    public void testIsSerializable() {
        assertEquals(workingHours, TestingUtils.serializeAndDeserialize(workingHours));
    }
}
