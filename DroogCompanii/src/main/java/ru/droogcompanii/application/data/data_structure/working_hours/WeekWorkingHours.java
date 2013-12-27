package ru.droogcompanii.application.data.data_structure.working_hours;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.droogcompanii.application.data.data_structure.working_hours.day_of_week_to_string_convertor.DayOfWeekToStringConvertor;
import ru.droogcompanii.application.data.data_structure.working_hours.day_of_week_to_string_convertor.DayOfWeekToStringConvertorProvider;
import ru.droogcompanii.application.util.StringsCombiner;

/**
 * Created by Leonid on 19.12.13.
 */
public class WeekWorkingHours implements Serializable {
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
        WorkingHours workingHours = workingHoursOfDay(someDay);
        return workingHours.includes(Time.from(someDay));
    }

    private WorkingHours workingHoursOfDay(Calendar someDay) {
        return workingHoursOfDay(someDay.get(Calendar.DAY_OF_WEEK));
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
        DayOfWeekToStringConvertor dayOfWeekToStringConvertor =
                DayOfWeekToStringConvertorProvider.getCurrentConvertor();
        List<String> lines = new ArrayList<String>();
        for (int dayOfWeek : DateTimeConstants.getDaysOfWeek()) {
            String nameOfDay = dayOfWeekToStringConvertor.dayOfWeekToString(dayOfWeek);
            WorkingHours workingHoursOfDay = workingHoursForDaysOfWeek.get(dayOfWeek);
            String line = nameOfDay + ":  " + workingHoursOfDay;
            lines.add(line);
        }
        return StringsCombiner.combine(lines);
    }
}
