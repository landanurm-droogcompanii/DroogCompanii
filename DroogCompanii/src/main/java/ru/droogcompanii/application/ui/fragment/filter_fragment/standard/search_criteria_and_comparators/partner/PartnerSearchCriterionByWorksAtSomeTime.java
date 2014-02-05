package ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators.partner;

import java.io.Serializable;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators.CalendarProvider;
import ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators.partner_point.PartnerPointSearchCriterionByWorksAtSomeTime;

/**
 * Created by ls on 16.01.14.
 */
public class PartnerSearchCriterionByWorksAtSomeTime implements SearchCriterion<Partner>, Serializable {

    private final SearchCriterion<PartnerPoint> searchCriterion;

    public PartnerSearchCriterionByWorksAtSomeTime(CalendarProvider calendarProvider) {
        searchCriterion = new PartnerPointSearchCriterionByWorksAtSomeTime(calendarProvider);
    }

    @Override
    public boolean meetCriterion(Partner partner) {
        for (PartnerPoint partnerPoint : PartnerPointsProviderWithoutContext.getPointsOf(partner)) {
            if (searchCriterion.meetCriterion(partnerPoint)) {
                return true;
            }
        }
        return false;
    }
}
