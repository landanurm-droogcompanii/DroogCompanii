package ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by ls on 16.01.14.
 */
class WorksNowFilter extends WorksForSomeTimeFilter {

    public WorksNowFilter() {
        super(new NowCalendarProvider());
    }
}

class NowCalendarProvider implements WorksForSomeTimeFilter.CalendarProvider, Serializable {
    @Override
    public Calendar getCalendar() {
        return Calendar.getInstance();
    }
}