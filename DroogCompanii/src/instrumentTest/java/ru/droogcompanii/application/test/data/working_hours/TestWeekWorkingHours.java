package ru.droogcompanii.application.test.data.working_hours;

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ru.droogcompanii.application.data.time.TimeOfDay;
import ru.droogcompanii.application.data.time.TimeRangeIncludedExcluded;
import ru.droogcompanii.application.data.working_hours.DateTimeConstants;
import ru.droogcompanii.application.data.working_hours.WeekWorkingHours;
import ru.droogcompanii.application.data.working_hours.WorkingHours;
import ru.droogcompanii.application.data.working_hours.WorkingHoursForEachDayOfWeek;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.DayAndNightWorkingHours;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.WorkingHoursOnBusinessDay;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.WorkingHoursOnHoliday;
import ru.droogcompanii.application.test.TestingUtils;
import ru.droogcompanii.application.util.IteratorOverTimes;

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

        workingHoursForEachDayOfWeek = new WorkingHoursForEachDayOfWeek();
        workingHoursForEachDayOfWeek.onMonday = new WorkingHoursOnHoliday("Monday - Holiday");
        workingHoursForEachDayOfWeek.onTuesday = new DayAndNightWorkingHours("Day&Night");
        workingHoursForEachDayOfWeek.onWednesday = new WorkingHoursOnBusinessDay()
                .include(new TimeRangeIncludedExcluded(new TimeOfDay(9, 0), new TimeOfDay(20, 0)));
        workingHoursForEachDayOfWeek.onThursday = new WorkingHoursOnBusinessDay()
                .include(new TimeRangeIncludedExcluded(new TimeOfDay(9, 0), new TimeOfDay(19, 0)));
        workingHoursForEachDayOfWeek.onFriday = new DayAndNightWorkingHours("All day");
        workingHoursForEachDayOfWeek.onSaturday = new WorkingHoursOnBusinessDay()
                .include(new TimeRangeIncludedExcluded(new TimeOfDay(8, 0), new TimeOfDay(19, 30)));
        workingHoursForEachDayOfWeek.onSunday = new WorkingHoursOnHoliday("Holiday");

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
            public void onEach(TimeOfDay time) {
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
        Calendar calendar = prepareCalendar(dayOfWeek, new TimeOfDay(10, 0));
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

    private Calendar prepareCalendar(int dayOfWeek, TimeOfDay time) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.HOUR_OF_DAY, time.getHours());
        calendar.set(Calendar.MINUTE, time.getMinutes());
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

    public void testIsSerializable() {
        assertEquals(weekWorkingHours, TestingUtils.serializeAndDeserialize(weekWorkingHours));
    }
}
