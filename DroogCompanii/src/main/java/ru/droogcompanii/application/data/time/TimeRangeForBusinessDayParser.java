package ru.droogcompanii.application.data.time;

import ru.droogcompanii.application.data.working_hours.working_hours_impl.WorkingHoursOnBusinessDay;

/**
 * Created by ls on 16.01.14.
 */
public class TimeRangeForBusinessDayParser {

    private static final int NUMBER_OF_RANGE_COMPONENTS = 2;

    public TimeRange parse(String text) {
        try {
            return tryParse(text);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot parse: " + text);
        }
    }

    private TimeRange tryParse(String text) {
        text = text.trim();
        String[] components = text.split(TimeRange.SEPARATOR);
        if (components.length != NUMBER_OF_RANGE_COMPONENTS) {
            throw new IllegalArgumentException();
        }
        TimeParser timeParser = new TimeParser();
        TimeOfDay from = timeParser.parse(components[0]);
        TimeOfDay to = timeParser.parse(components[1]);
        return WorkingHoursOnBusinessDay.createTimeRange(from, to);
    }
}
