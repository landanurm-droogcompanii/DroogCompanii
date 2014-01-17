package ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter;

import java.io.Serializable;
import java.util.Calendar;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.view.activity_3.filter_activity.filter.SearchFilter;

/**
 * Created by ls on 16.01.14.
 */
class WorksForSomeTimeFilter extends SearchFilter<PartnerPoint> implements Serializable {

    static interface CalendarProvider {
        Calendar getCalendar();
    }

    private CalendarProvider calendarProvider;

    public WorksForSomeTimeFilter(CalendarProvider calendarProvider) {
        this.calendarProvider = calendarProvider;
    }

    @Override
    public boolean meetCriteria(PartnerPoint partnerPoint) {
        Calendar calendar = calendarProvider.getCalendar();
        return partnerPoint.workingHours.includes(calendar);
    }
}
