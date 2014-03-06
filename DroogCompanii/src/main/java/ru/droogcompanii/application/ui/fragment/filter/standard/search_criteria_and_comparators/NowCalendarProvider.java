package ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators;

import java.io.Serializable;
import java.util.Calendar;

import ru.droogcompanii.application.util.CalendarUtils;

/**
 * Created by ls on 05.02.14.
 */
public class NowCalendarProvider implements CalendarProvider, Serializable {
    @Override
    public Calendar getCalendar() {
        return CalendarUtils.now();
    }
}
