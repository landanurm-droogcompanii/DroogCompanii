package ru.droogcompanii.application.data.data_structure.working_hours;

import junit.framework.TestCase;

import java.util.Calendar;

import ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl.WorkingHoursBuilder;

/**
 * Created by ls on 09.01.14.
 */
public class TestWeekWorkingHours extends TestCase {

    private WorkingHoursForEachDayOfWeek workingHoursForEachDayOfWeek;
    private WeekWorkingHours weekWorkingHours;
    private WeekWorkingHours copy;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        WorkingHoursBuilder workingHoursBuilder = new WorkingHoursBuilder();
        workingHoursForEachDayOfWeek = new WorkingHoursForEachDayOfWeek();
        workingHoursForEachDayOfWeek.onMonday = workingHoursBuilder.onHoliday("Monday");
        workingHoursForEachDayOfWeek.onTuesday = workingHoursBuilder.buildDayAndNightWorkingHours();
        workingHoursForEachDayOfWeek.onWednesday = workingHoursBuilder.onBusinessDay(new Time(9, 0), new Time(20, 0));
        workingHoursForEachDayOfWeek.onThursday = workingHoursBuilder.onBusinessDay(new Time(9, 0), new Time(19, 0));
        workingHoursForEachDayOfWeek.onFriday = workingHoursBuilder.buildDayAndNightWorkingHours();
        workingHoursForEachDayOfWeek.onSaturday = workingHoursBuilder.onBusinessDay(new Time(8, 0), new Time(19, 30));
        workingHoursForEachDayOfWeek.onSunday = workingHoursBuilder.onHoliday("Holiday");

        weekWorkingHours = new WeekWorkingHours(workingHoursForEachDayOfWeek);
        copy = new WeekWorkingHours(workingHoursForEachDayOfWeek);
    }


    public void testIncludes() {
        final int[] daysOfWeek = DateTimeConstants.getDaysOfWeek();
        final WorkingHours[] workingHours = new WorkingHours[] {
                workingHoursForEachDayOfWeek.onMonday,
                workingHoursForEachDayOfWeek.onTuesday,
                workingHoursForEachDayOfWeek.onWednesday,
                workingHoursForEachDayOfWeek.onThursday,
                workingHoursForEachDayOfWeek.onFriday,
                workingHoursForEachDayOfWeek.onSaturday,
                workingHoursForEachDayOfWeek.onSunday
        };
        IteratorOverTimes.iterateAllTimesIn24Hours(new IteratorOverTimes.OnEachHandler() {
            @Override
            public void onEach(Time time) {
                for (int i = 0; i < daysOfWeek.length; ++i) {
                    int dayOfWeek = daysOfWeek[i];
                    WorkingHours workingHoursOfDay = workingHours[i];
                    Calendar calendar = prepareCalendar(dayOfWeek, time);
                    assertEquals(workingHoursOfDay.includes(time),
                                 weekWorkingHours.includes(calendar));
                }
            }
        });
    }

    private Calendar prepareCalendar(int dayOfWeek, Time time) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, time.hours);
        calendar.set(Calendar.MINUTE, time.minutes);
        return calendar;
    }

    public void testEquals() {
        assertEquals(weekWorkingHours, copy);
    }

    public void testEqualsToItself() {
        assertEquals(weekWorkingHours, weekWorkingHours);
    }

    public void testNotEqualsToNull() {
        assertFalse(weekWorkingHours.equals(null));
    }
}
