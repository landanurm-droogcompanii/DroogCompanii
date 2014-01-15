package ru.droogcompanii.application.data.data_structure.working_hours;

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl.WorkingHoursBuilder;
import ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl.WorkingHoursOnHoliday;

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

    public void testDoesNotIncludeHoliday() {
        int dayOfWeek = findDayOfWeekOfHoliday();
        Calendar calendar = prepareCalendar(dayOfWeek, new Time(10, 0));
        assertFalse(weekWorkingHours.includes(calendar));
    }

    private int findDayOfWeekOfHoliday() {
        Map<Integer,WorkingHours> pairs_DayOfWeek_WorkingHours = new HashMap<Integer, WorkingHours>();
        pairs_DayOfWeek_WorkingHours.put(Calendar.MONDAY, workingHoursForEachDayOfWeek.onMonday);
        pairs_DayOfWeek_WorkingHours.put(Calendar.TUESDAY, workingHoursForEachDayOfWeek.onTuesday);
        pairs_DayOfWeek_WorkingHours.put(Calendar.WEDNESDAY, workingHoursForEachDayOfWeek.onWednesday);
        pairs_DayOfWeek_WorkingHours.put(Calendar.THURSDAY, workingHoursForEachDayOfWeek.onThursday);
        pairs_DayOfWeek_WorkingHours.put(Calendar.FRIDAY, workingHoursForEachDayOfWeek.onFriday);
        pairs_DayOfWeek_WorkingHours.put(Calendar.SATURDAY, workingHoursForEachDayOfWeek.onSaturday);
        pairs_DayOfWeek_WorkingHours.put(Calendar.SUNDAY, workingHoursForEachDayOfWeek.onSunday);
        for (Map.Entry<Integer,WorkingHours> each : pairs_DayOfWeek_WorkingHours.entrySet()) {
            int dayOfWeek = each.getKey();
            WorkingHours workingHours = each.getValue();
            if (holiday(workingHours)) {
                return dayOfWeek;
            }
        }
        throw new IllegalStateException("No holidays in tested WeekWorkingHours instance");
    }

    private boolean holiday(WorkingHours workingHours) {
        return workingHours instanceof WorkingHoursOnHoliday;
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
