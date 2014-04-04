package ru.droogcompanii.application.data.working_hours;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.droogcompanii.application.data.time.TimeOfDay;
import ru.droogcompanii.application.data.working_hours.day_of_week_to_string_converter.DayOfWeekToStringConverter;
import ru.droogcompanii.application.data.working_hours.day_of_week_to_string_converter.DayOfWeekToStringConverterProvider;
import ru.droogcompanii.application.util.StringsCombiner;

/**
 * Created by Leonid on 19.12.13.
 */
public class WeekWorkingHours implements Serializable {
    public static final String SEPARATOR_BETWEEN_DAY_AND_WORKING_HOURS = ":  ";

    private final Map<Integer, WorkingHours> workingHoursForDaysOfWeek;

    public WeekWorkingHours(WorkingHoursForEachDayOfWeek workingHours) {
        workingHoursForDaysOfWeek = new HashMap<Integer, WorkingHours>();
        workingHoursForDaysOfWeek.put(Calendar.MONDAY, workingHours.onMonday);
        workingHoursForDaysOfWeek.put(Calendar.TUESDAY, workingHours.onTuesday);
        workingHoursForDaysOfWeek.put(Calendar.WEDNESDAY, workingHours.onWednesday);
        workingHoursForDaysOfWeek.put(Calendar.THURSDAY, workingHours.onThursday);
        workingHoursForDaysOfWeek.put(Calendar.FRIDAY, workingHours.onFriday);
        workingHoursForDaysOfWeek.put(Calendar.SATURDAY, workingHours.onSaturday);
        workingHoursForDaysOfWeek.put(Calendar.SUNDAY, workingHours.onSunday);
    }

    public boolean includes(Calendar someDay) {
        WorkingHours workingHours = workingHoursOfCalendar(someDay);
        return workingHours.isInclude(TimeOfDay.fromCalendar(someDay));
    }

    private WorkingHours workingHoursOfCalendar(Calendar calendar) {
        return workingHoursOfDay(calendar.get(Calendar.DAY_OF_WEEK));
    }

    public WorkingHours workingHoursOfDay(int dayOfWeek) {
        return workingHoursForDaysOfWeek.get(dayOfWeek);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof WeekWorkingHours)) {
            return false;
        }
        WeekWorkingHours other = (WeekWorkingHours) obj;
        return workingHoursForDaysOfWeek.equals( other.workingHoursForDaysOfWeek );
    }

    @Override
    public int hashCode() {
        return workingHoursForDaysOfWeek.hashCode();
    }

    @Override
    public String toString() {
        DayOfWeekToStringConverter dayOfWeekToStringConverter =
                DayOfWeekToStringConverterProvider.getCurrentConverter();
        List<String> lines = new ArrayList<String>();
        for (int dayOfWeek : DateTimeConstants.getDaysOfWeek()) {
            String nameOfDay = dayOfWeekToStringConverter.dayOfWeekToString(dayOfWeek);
            WorkingHours workingHoursOfDay = workingHoursForDaysOfWeek.get(dayOfWeek);
            String line = nameOfDay + SEPARATOR_BETWEEN_DAY_AND_WORKING_HOURS + workingHoursOfDay;
            lines.add(line);
        }
        return StringsCombiner.combine(lines);
    }

    public WorkingHours getWorkingHoursOf(int dayOfWeek) {
        return workingHoursForDaysOfWeek.get(dayOfWeek);
    }

    public WorkingHours getWorkingHoursOf(Calendar calendar) {
        return workingHoursForDaysOfWeek.get(calendar.get(Calendar.DAY_OF_WEEK));
    }
}
