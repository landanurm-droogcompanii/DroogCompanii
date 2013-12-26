package ru.droogcompanii.application.data.data_structure.working_hours;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Leonid on 19.12.13.
 */
public class Time implements Serializable {
    public final int hours;
    public final int minutes;

    public static Time from(Calendar calendar) {
        int hoursIn24HourFormat = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        return new Time(hoursIn24HourFormat, minutes);
    }

    public Time(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Time)) {
            return false;
        }
        Time other = (Time) obj;
        return (hours == other.hours) &&
               (minutes == other.minutes);
    }

    @Override
    public int hashCode() {
        return toMinutesOfDay();
    }

    private int toMinutesOfDay() {
        return hours * 60 + minutes;
    }

    @Override
    public String toString() {
        return twoDigitString(hours) + ":" + twoDigitString(minutes);
    }

    private static String twoDigitString(int timeComponent) {
        return (timeComponent < 10)
                ? ("0" + timeComponent)
                : String.valueOf(timeComponent);
    }

    public boolean before(Time time) {
        return toMinutesOfDay() < time.toMinutesOfDay();
    }

    public boolean after(Time time) {
        return toMinutesOfDay() > time.toMinutesOfDay();
    }
}
