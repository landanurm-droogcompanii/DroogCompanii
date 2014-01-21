package ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.filters;

import java.io.Serializable;
import java.util.Calendar;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.filter.SearchFilter;

/**
 * Created by ls on 16.01.14.
 */
public class WorksAtSomeTimeFilter extends SearchFilter<PartnerPoint> implements Serializable {

    public static interface CalendarProvider {
        Calendar getCalendar();
    }

    private CalendarProvider calendarProvider;

    public WorksAtSomeTimeFilter(CalendarProvider calendarProvider) {
        this.calendarProvider = calendarProvider;
    }

    @Override
    public boolean meetCriterion(PartnerPoint partnerPoint) {
        Calendar calendar = calendarProvider.getCalendar();
        return partnerPoint.workingHours.includes(calendar);
    }
}
