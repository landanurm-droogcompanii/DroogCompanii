package ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.search_criteria_and_comparators;

import java.io.Serializable;
import java.util.Calendar;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;

/**
 * Created by ls on 16.01.14.
 */
public class SearchCriterionByWorksAtSomeTime
        implements SearchableListing.SearchCriterion<PartnerPoint>, Serializable {

    public static interface CalendarProvider {
        Calendar getCalendar();
    }

    private CalendarProvider calendarProvider;

    public SearchCriterionByWorksAtSomeTime(CalendarProvider calendarProvider) {
        this.calendarProvider = calendarProvider;
    }

    @Override
    public boolean meetCriterion(PartnerPoint partnerPoint) {
        Calendar calendar = calendarProvider.getCalendar();
        return partnerPoint.workingHours.includes(calendar);
    }
}
