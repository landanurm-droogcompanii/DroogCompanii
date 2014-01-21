package ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.search_criteria_and_comparators;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by ls on 16.01.14.
 */
public class WorksNowSearchCriterion extends WorksAtSomeTimeSearchCriterion {

    public WorksNowSearchCriterion() {
        super(new NowCalendarProvider());
    }

    static class NowCalendarProvider implements WorksAtSomeTimeSearchCriterion.CalendarProvider, Serializable {
        @Override
        public Calendar getCalendar() {
            return Calendar.getInstance();
        }
    }
}
