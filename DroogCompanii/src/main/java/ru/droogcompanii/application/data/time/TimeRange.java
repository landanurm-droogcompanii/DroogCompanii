package ru.droogcompanii.application.data.time;

import java.io.Serializable;

/**
 * Created by ls on 16.01.14.
 */

class TimeOfDayCalculator {
    private static final int MINUTES_PER_HOUR = 60;

    private static final TimeOfDay MIDNIGHT = new TimeOfDay(0, 0);
    private static final TimeOfDay ONE_MINUTE_BEFORE_MIDNIGHT = new TimeOfDay(23, 59);

    private final TimeOfDay timeOfDay;

    TimeOfDayCalculator(TimeOfDay timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public TimeOfDay increasedByOneMinute() {
        if (timeOfDay.equals(ONE_MINUTE_BEFORE_MIDNIGHT)) {
            return MIDNIGHT;
        }
        int hours = timeOfDay.getHours();
        int minutes = timeOfDay.getMinutes() + 1;
        if (minutes < MINUTES_PER_HOUR) {
            return new TimeOfDay(hours, minutes);
        }
        return new TimeOfDay(hours + 1, 0);
    }

    public TimeOfDay decreasedByOneMinute() {
        if (timeOfDay.equals(MIDNIGHT)) {
            return ONE_MINUTE_BEFORE_MIDNIGHT;
        }
        int hours = timeOfDay.getHours();
        int minutes = timeOfDay.getMinutes() - 1;
        if (minutes >= 0) {
            return new TimeOfDay(hours, minutes);
        }
        return new TimeOfDay(hours - 1, 59);
    }
}

public class TimeRange implements Serializable {
    public static final String SEPARATOR = "-";

    private final TimeOfDay from;
    private final TimeOfDay to;

    public static TimeRange newIncludedIncluded(TimeOfDay from, TimeOfDay to) {
        return new TimeRange(from, to);
    }

    public static TimeRange newIncludedExcluded(TimeOfDay from, TimeOfDay to) {
        return new TimeRange(
                from,
                new TimeOfDayCalculator(to).decreasedByOneMinute());
    }

    public static TimeRange newExcludedIncluded(TimeOfDay from, TimeOfDay to) {
        return new TimeRange(
                new TimeOfDayCalculator(from).increasedByOneMinute(),
                to
        );
    }

    public static TimeRange newExcludedExcluded(TimeOfDay from, TimeOfDay to) {
        return new TimeRange(
                new TimeOfDayCalculator(from).increasedByOneMinute(),
                new TimeOfDayCalculator(to).decreasedByOneMinute()
        );
    }

    public TimeRange(TimeOfDay from, TimeOfDay to) {
        if (to.before(from)) {
            throw new IllegalArgumentException("time <from> must be before <to> time");
        }
        this.from = from;
        this.to = to;
    }

    public TimeOfDay from() {
        return from;
    }

    public TimeOfDay to() {
        return to;
    }

    public boolean includes(TimeOfDay time) {
        return time.within(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TimeRange)) {
            return false;
        }
        TimeRange other = (TimeRange) obj;
        return (from.equals(other.from())) &&
                (to.equals(other.to()));
    }

    @Override
    public int hashCode() {
        return from.hashCode() + to.hashCode();
    }

    @Override
    public String toString() {
        return from + SEPARATOR + to;
    }
}
