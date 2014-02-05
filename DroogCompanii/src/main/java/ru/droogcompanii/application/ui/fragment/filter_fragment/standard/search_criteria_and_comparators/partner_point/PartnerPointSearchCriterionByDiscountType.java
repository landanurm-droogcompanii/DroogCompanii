package ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators.partner_point;

import java.io.Serializable;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators.partner.PartnerSearchCriterionByDiscountType;

/**
 * Created by ls on 16.01.14.
 */

public class PartnerPointSearchCriterionByDiscountType
        implements SearchCriterion<PartnerPoint>, Serializable {

    private PartnerSearchCriterionByDiscountType searchCriterion;

    public PartnerPointSearchCriterionByDiscountType(boolean bonus, boolean discount, boolean cashBack) {
        searchCriterion = new PartnerSearchCriterionByDiscountType(bonus, discount, cashBack);
    }

    @Override
    public boolean meetCriterion(PartnerPoint partnerPoint) {
        Partner partner = PartnersProviderWithoutContext.getPartnerOf(partnerPoint);
        return searchCriterion.meetCriterion(partner);
    }
}