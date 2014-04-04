package ru.droogcompanii.application.data.working_hours;

import ru.droogcompanii.application.data.time.TimeOfDay;

/**
 * Created by Leonid on 19.12.13.
 */
public interface WorkingHours {
    boolean isInclude(TimeOfDay time);
}
