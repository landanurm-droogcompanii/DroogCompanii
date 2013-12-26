package ru.droogcompanii.application.data.data_structure.working_hours.day_of_week_to_string_convertor;

/**
 * Created by Leonid on 19.12.13.
 */
public interface DayOfWeekToStringConvertor {
    // dayOfWeek - some of Calendar constants such as Calendar.MONDAY, Calendar.TUESDAY etc.
    String dayOfWeekToString(int dayOfWeek);
}
