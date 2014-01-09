package ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl;

import junit.framework.TestCase;

import ru.droogcompanii.application.data.data_structure.working_hours.IteratorOverTimes;
import ru.droogcompanii.application.data.data_structure.working_hours.Time;
import ru.droogcompanii.application.data.data_structure.working_hours.WorkingHours;

/**
 * Created by ls on 09.01.14.
 */
public class TestWorkingHoursBuilder extends TestCase {

    private WorkingHoursBuilder workingHoursBuilder;

    private Time from;
    private Time to;
    private WorkingHours workingHours;


    @Override
    public void setUp() throws Exception {
        super.setUp();

        workingHoursBuilder = new WorkingHoursBuilder();

        from = new Time(6, 30);
        to = new Time(18, 20);
    }

    public void testCreatesCorrectWorkingHoursOnBusinessDay() {
        workingHours = workingHoursBuilder.onBusinessDay(from, to);
        assertTrue(workingHours instanceof WorkingHoursOnBusinessDay);
        String expectedString = from + WorkingHoursOnBusinessDay.SEPARATOR_BETWEEN_TIMES + to;
        assertEquals(expectedString, workingHours.toString());
        assertWorkingHoursOnBusinessDayMethodIncludesWorksCorrectly();
    }

    private void assertWorkingHoursOnBusinessDayMethodIncludesWorksCorrectly() {
        IteratorOverTimes.iterateAllTimesIn24Hours(new IteratorOverTimes.OnEachHandler() {
            @Override
            public void onEach(Time time) {
                assertEquals(time.within(from, to), workingHours.includes(time));
            }
        });
    }

    public void testCreatesCorrectWorkingHoursOnBusinessDay_IfFromAndToEqualMidnight() {
        final Time TIME_00_00 = new Time(0, 0);
        workingHours = workingHoursBuilder.onBusinessDay(TIME_00_00, TIME_00_00);
        assertTrue(workingHours instanceof DayAndNightWorkingHours);
        assertDayAndNightWorkingHoursMethodIncludesWorksCorrectly();
    }

    private void assertDayAndNightWorkingHoursMethodIncludesWorksCorrectly() {
        IteratorOverTimes.iterateAllTimesIn24Hours(new IteratorOverTimes.OnEachHandler() {
            @Override
            public void onEach(Time time) {
                assertTrue(workingHours.includes(time));
            }
        });
    }

    public void testCreatesCorrectWorkingHoursOnHoliday() {
        String message = "Message";
        workingHours = workingHoursBuilder.onHoliday(message);
        assertEquals(new WorkingHoursOnHoliday(message), workingHours);
    }

    public void testCreatesCorrectDayAndNightWorkingHours() {
        WorkingHours workingHours = workingHoursBuilder.buildDayAndNightWorkingHours();
        assertTrue(workingHours != null);
        assertTrue(workingHours instanceof DayAndNightWorkingHours);
    }


}
