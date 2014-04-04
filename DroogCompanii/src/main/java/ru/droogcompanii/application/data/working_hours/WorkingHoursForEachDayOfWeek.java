package ru.droogcompanii.application.data.working_hours;

import java.util.Calendar;

/**
 * Created by Leonid on 19.12.13.
 */
public class WorkingHoursForEachDayOfWeek {
    public WorkingHours onMonday;
    public WorkingHours onTuesday;
    public WorkingHours onWednesday;
    public WorkingHours onThursday;
    public WorkingHours onFriday;
    public WorkingHours onSaturday;
    public WorkingHours onSunday;

    public void set(int dayOfWeek, WorkingHours workingHours) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                onMonday = workingHours;
                break;

            case Calendar.TUESDAY:
                onTuesday = workingHours;
                break;

            case Calendar.WEDNESDAY:
                onWednesday = workingHours;
                break;

            case Calendar.THURSDAY:
                onThursday = workingHours;
                break;

            case Calendar.FRIDAY:
                onFriday = workingHours;
                break;

            case Calendar.SATURDAY:
                onSaturday = workingHours;
                break;

            case Calendar.SUNDAY:
                onSunday = workingHours;
                break;

            default:
                throw new IllegalArgumentException("Unknown day of week: " + dayOfWeek);
        }
    }

    public WorkingHours get(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return onMonday;

            case Calendar.TUESDAY:
                return onTuesday;

            case Calendar.WEDNESDAY:
                return onWednesday;

            case Calendar.THURSDAY:
                return onThursday;

            case Calendar.FRIDAY:
                return onFriday;

            case Calendar.SATURDAY:
                return onSaturday;

            case Calendar.SUNDAY:
                return onSunday;

            default:
                throw new IllegalArgumentException("Unknown day of week: " + dayOfWeek);
        }
    }
}
