package ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.filters;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by ls on 16.01.14.
 */
public class WorksNowFilter extends WorksAtSomeTimeFilter {

    public WorksNowFilter() {
        super(new NowCalendarProvider());
    }

    static class NowCalendarProvider implements WorksAtSomeTimeFilter.CalendarProvider, Serializable {
        @Override
        public Calendar getCalendar() {
            return Calendar.getInstance();
        }
    }
}
