package ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl;

import java.io.Serializable;

import ru.droogcompanii.application.data.data_structure.working_hours.Time;
import ru.droogcompanii.application.data.data_structure.working_hours.WorkingHours;

/**
 * Created by Leonid on 19.12.13.
 */
class WorkingHoursOnBusinessDay implements WorkingHours, Serializable {
    private final Time from;
    private final Time to;

    public WorkingHoursOnBusinessDay(Time from, Time to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean includes(Time time) {
        return (!from.after(time)) && (!to.before(time));
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
        return (from.equals(other.from)) &&
               (to.equals(other.to));
    }

    @Override
    public int hashCode() {
        return from.hashCode() + to.hashCode();
    }

    @Override
    public String toString() {
        return from + "-" + to;
    }
}
