package ru.droogcompanii.application.data.data_structure.working_hours.time;

import java.io.Serializable;

/**
 * Created by ls on 16.01.14.
 */
public class TimeRange implements Serializable {
    public static final String SEPARATOR = "-";

    private final Time from;
    private final Time to;

    public TimeRange(Time from, Time to) {
        if (to.before(from)) {
            throw new IllegalArgumentException("time <from> must be before <to> time");
        }
        this.from = from;
        this.to = to;
    }

    public Time from() {
        return from;
    }

    public Time to() {
        return to;
    }

    public boolean includes(Time time) {
        return time.within(from, to);
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
