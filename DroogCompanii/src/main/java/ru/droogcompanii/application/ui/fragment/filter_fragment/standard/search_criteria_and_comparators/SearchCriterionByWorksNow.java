package ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by ls on 16.01.14.
 */
public class SearchCriterionByWorksNow extends SearchCriterionByWorksAtSomeTime {

    public SearchCriterionByWorksNow() {
        super(new NowCalendarProvider());
    }

    static class NowCalendarProvider implements SearchCriterionByWorksAtSomeTime.CalendarProvider, Serializable {
        @Override
        public Calendar getCalendar() {
            return Calendar.getInstance();
        }
    }
}
