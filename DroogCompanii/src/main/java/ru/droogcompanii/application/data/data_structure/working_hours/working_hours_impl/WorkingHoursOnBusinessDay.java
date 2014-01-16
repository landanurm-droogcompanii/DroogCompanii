package ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl;

import java.io.Serializable;

import ru.droogcompanii.application.data.data_structure.working_hours.WorkingHours;
import ru.droogcompanii.application.data.data_structure.working_hours.time.Time;
import ru.droogcompanii.application.data.data_structure.working_hours.time.TimeRange;

/**
 * Created by Leonid on 19.12.13.
 */
public class WorkingHoursOnBusinessDay implements WorkingHours, Serializable {
    public static final String SEPARATOR_BETWEEN_TIMES = "-";

    private final TimeRange workingTimeRange;

    public WorkingHoursOnBusinessDay(TimeRange timeRange) {
        this(timeRange.from(), timeRange.to());
    }

    public WorkingHoursOnBusinessDay(Time from, Time to) {
        workingTimeRange = new TimeRange(from, to);
    }

    @Override
    public boolean includes(Time time) {
        return workingTimeRange.includes(time);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof WorkingHoursOnBusinessDay)) {
            return false;
        }
        WorkingHoursOnBusinessDay other = (WorkingHoursOnBusinessDay) obj;
        return workingTimeRange.equals(other.workingTimeRange);
    }

    @Override
    public int hashCode() {
        return workingTimeRange.hashCode();
    }

    @Override
    public String toString() {
        return workingTimeRange.toString();
    }
}
