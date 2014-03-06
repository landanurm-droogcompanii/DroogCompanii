package ru.droogcompanii.application.util;

import java.util.Calendar;

public class CalendarUtils {
    public static final String SEPARATOR_OF_DATE_COMPONENTS = ".";

	public static Calendar createByDayMonthYear(int dayOfMonth, int month, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

    public static Calendar createByMilliseconds(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return calendar;
    }

    public static String convertToString(Calendar calendar) {
        return (toTwoDigitString(calendar.get(Calendar.DAY_OF_MONTH)) +
                SEPARATOR_OF_DATE_COMPONENTS +
                toTwoDigitString(calendar.get(Calendar.MONTH)) +
                SEPARATOR_OF_DATE_COMPONENTS +
                calendar.get(Calendar.YEAR));
    }

    private static String toTwoDigitString(int value) {
        return ((value < 10) ? "0" : "") + value;
    }

    public static Calendar now() {
        return Calendar.getInstance();
    }
}
