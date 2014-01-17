package ru.droogcompanii.application.util;

import ru.droogcompanii.application.data.time.TimeOfDay;

/**
 * Created by ls on 09.01.14.
 */
public class IteratorOverTimes {
    private static final int MINUTES_PER_HOUR = 60;

    public static interface OnEachHandler {
        void onEach(TimeOfDay time);
    }

    public static void iterateAllTimesIn24Hours(OnEachHandler onEachHandler) {
        iterateAllHours(24, onEachHandler);
    }

    private static void iterateAllHours(int hours, OnEachHandler onEachHandler) {
        for (int hour = 0; hour < hours; ++hour) {
            for (int minute = 0; minute < MINUTES_PER_HOUR; ++minute) {
                onEachHandler.onEach(new TimeOfDay(hour, minute));
            }
        }
    }
}
