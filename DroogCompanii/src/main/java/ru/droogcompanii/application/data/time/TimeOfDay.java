package ru.droogcompanii.application.data.time;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Leonid on 19.12.13.
 */
public class TimeOfDay implements Time, Serializable {
    private static final int HOURS_PER_DAY = 24;
    private static final int MINUTES_PER_HOUR = 60;

    //private final int hours;
    private final int minutes;

    public static TimeOfDay from(Calendar calendar) {
        int hoursIn24HourFormat = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        return new TimeOfDay(hoursIn24HourFormat, minutes);
    }

    public TimeOfDay(int hours, int minutes) {
        checkArguments(hours, minutes);
        //this.hours = hours;
        //this.minutes = minutes;
        /* + */
        this.minutes = hours * MINUTES_PER_HOUR + minutes;
    }

    private void checkArguments(int hours, int minutes) {
        if (hours < 0 || hours >= HOURS_PER_DAY || minutes < 0 || minutes >= MINUTES_PER_HOUR) {
            throw new IllegalArgumentException("Illegal time: <" + hours + ":" + minutes + ">");
        }
    }

    @Override
    public int getHours() {
        //return hours;
        /* + */
        return minutes / MINUTES_PER_HOUR;
    }

    @Override
    public int getMinutes() {
        //return minutes;
        /* + */
        return minutes % MINUTES_PER_HOUR;
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
        return (getHours() == other.getHours()) &&
               (getMinutes() == other.getMinutes());

    }

    @Override
    public int hashCode() {
        return toMinutesOfDay();
    }

    private int toMinutesOfDay() {
        //return hours * 60 + minutes;
        /* + */
        return minutes;
    }

    @Override
    public String toString() {
        return twoDigitString(getHours()) + ":" + twoDigitString(getMinutes());
    }

    private static String twoDigitString(int timeComponent) {
        return (timeComponent < 10)
                ? ("0" + timeComponent)
                : String.valueOf(timeComponent);
    }

    public boolean before(TimeOfDay time) {
        return this.toMinutesOfDay() < time.toMinutesOfDay();
    }

    public boolean after(TimeOfDay time) {
        return this.toMinutesOfDay() > time.toMinutesOfDay();
    }

    public boolean within(TimeRangeIncludedExcluded timeRange) {
        return timeRange.includes(this);
    }
}
