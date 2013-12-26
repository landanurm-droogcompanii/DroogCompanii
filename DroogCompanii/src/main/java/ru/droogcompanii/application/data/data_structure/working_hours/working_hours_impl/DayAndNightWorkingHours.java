package ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl;

import java.io.Serializable;

import ru.droogcompanii.application.data.data_structure.working_hours.Time;
import ru.droogcompanii.application.data.data_structure.working_hours.WorkingHours;

/**
 * Created by Leonid on 19.12.13.
 */
class DayAndNightWorkingHours implements WorkingHours, Serializable {
    private final String messageToShow;

    public DayAndNightWorkingHours(String messageToShow) {
        this.messageToShow = messageToShow;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof DayAndNightWorkingHours);
    }

    @Override
    public int hashCode() {
        return messageToShow.hashCode() * 2;
    }

    @Override
    public String toString() {
        return messageToShow;
    }

    @Override
    public boolean includes(Time time) {
        return true;
    }
}
