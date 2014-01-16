package ru.droogcompanii.application.data.data_structure.working_hours;

import ru.droogcompanii.application.data.data_structure.working_hours.time.Time;

/**
 * Created by ls on 09.01.14.
 */
public class IteratorOverTimes {
    public static interface OnEachHandler {
        void onEach(Time time);
    }

    public static void iterateAllTimesIn24Hours(OnEachHandler onEachHandler) {
        final int HOURS_PER_DAY = 24;
        final int MINUTES_PER_HOUR = 60;
        for (int hour = 0; hour < HOURS_PER_DAY; ++hour) {
            for (int minute = 0; minute < MINUTES_PER_HOUR; ++minute) {
                onEachHandler.onEach(new Time(hour, minute));
            }
        }
    }
}
