package ru.droogcompanii.application.data.data_structure.working_hours;

import ru.droogcompanii.application.data.data_structure.working_hours.time.Time;

/**
 * Created by Leonid on 19.12.13.
 */
public interface WorkingHours {
    boolean includes(Time time);
}
