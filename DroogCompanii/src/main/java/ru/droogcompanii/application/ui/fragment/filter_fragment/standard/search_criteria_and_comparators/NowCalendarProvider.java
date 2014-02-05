package ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by ls on 05.02.14.
 */
public class NowCalendarProvider implements CalendarProvider, Serializable {
    @Override
    public Calendar getCalendar() {
        Calendar now = Calendar.getInstance();
        return now;
    }
}
