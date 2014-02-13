package ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.util.MoreComparableString;
import ru.droogcompanii.application.util.StringConstants;

/**
 * Created by ls on 16.01.14.
 */

public class PartnerSearchCriterionByDiscountType
        implements SearchCriterion<Partner>, Serializable {

    private final List<String> discountTypes;

    public PartnerSearchCriterionByDiscountType(boolean bonus, boolean discount, boolean cashBack) {
        discountTypes = new ArrayList<String>();
        if (bonus) {
            discountTypes.add(StringConstants.DiscountType.BONUS);
        }
        if (discount) {
            discountTypes.add(StringConstants.DiscountType.DISCOUNT);
        }
        if (cashBack) {
            discountTypes.add(StringConstants.DiscountType.CASH_BACK);
        }
    }

    @Override
    public boolean meetCriterion(Partner partner) {
        MoreComparableString partnerPointDiscountType = new MoreComparableString(partner.getDiscountType());
        for (String discountType : discountTypes) {
            if (partnerPointDiscountType.containsIgnoreCase(discountType)) {
                return true;
            }
        }
        return false;
    }
}