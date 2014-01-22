package ru.droogcompanii.application.data.time;

import java.io.Serializable;

/**
 * Created by ls on 16.01.14.
 */


public class TimeRangeIncludedExcluded implements TimeRange, Serializable {
    private final TimeOfDay from;
    private final TimeOfDay to;

    public TimeRangeIncludedExcluded(TimeOfDay from, TimeOfDay to) {
        checkArguments(from, to);
        this.from = from;
        this.to = to;
    }

    private void checkArguments(TimeOfDay from, TimeOfDay to) {
        if (to.before(from)) {
            throw new IllegalArgumentException("time <from> must be before <to> time");
        }
    }

    public TimeOfDay from() {
        return from;
    }

    public TimeOfDay to() {
        return to;
    }

    public boolean includes(TimeOfDay time) {
        return !from.after(time) && to.after(time);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TimeRangeIncludedExcluded)) {
            return false;
        }
        TimeRangeIncludedExcluded other = (TimeRangeIncludedExcluded) obj;
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
