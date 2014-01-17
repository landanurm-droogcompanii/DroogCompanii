package ru.droogcompanii.application.data.working_hours.working_hours_impl;

import java.io.Serializable;

import ru.droogcompanii.application.data.time.TimeOfDay;
import ru.droogcompanii.application.data.working_hours.WorkingHours;

/**
 * Created by Leonid on 19.12.13.
 */
public class DayAndNightWorkingHours implements WorkingHours, Serializable {
    private final String messageToShow;

    public DayAndNightWorkingHours(String messageToShow) {
        this.messageToShow = messageToShow;
    }

    @Override
    public boolean includes(TimeOfDay time) {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DayAndNightWorkingHours)) {
            return false;
        }
        DayAndNightWorkingHours other = (DayAndNightWorkingHours) obj;
        return messageToShow.equals(other.messageToShow);
    }

    @Override
    public int hashCode() {
        return messageToShow.hashCode();
    }

    @Override
    public String toString() {
        return messageToShow;
    }
}
