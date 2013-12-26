package ru.droogcompanii.application.data.data_structure.working_hours;

import java.util.Calendar;

/**
 * Created by ls on 23.12.13.
 */
public class DateTimeConstants {
    private static final int[] daysOfWeek = new int[] {
        Calendar.MONDAY,
        Calendar.TUESDAY,
        Calendar.WEDNESDAY,
        Calendar.THURSDAY,
        Calendar.FRIDAY,
        Calendar.SATURDAY,
        Calendar.SUNDAY
    };

    public static int[] getDaysOfWeek() {
        return daysOfWeek;
    }
}
