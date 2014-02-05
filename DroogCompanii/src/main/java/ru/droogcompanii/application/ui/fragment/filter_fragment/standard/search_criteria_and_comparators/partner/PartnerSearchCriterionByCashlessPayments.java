package ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators.partner;

import java.io.Serializable;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators.partner_point.PartnerPointSearchCriterionByCashlessPayments;

/**
 * Created by ls on 16.01.14.
 */

public class PartnerSearchCriterionByCashlessPayments
        implements SearchCriterion<Partner>, Serializable {
    @Override
    public boolean meetCriterion(Partner partner) {
        SearchCriterion<PartnerPoint> searchCriterion = new PartnerPointSearchCriterionByCashlessPayments();
        for (PartnerPoint partnerPoint : PartnerPointsProviderWithoutContext.getPointsOf(partner)) {
            if (searchCriterion.meetCriterion(partnerPoint)) {
                return true;
            }
        }
        return false;
    }
}
