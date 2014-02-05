package ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators.partner_point;

import java.io.Serializable;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.util.MoreComparableString;
import ru.droogcompanii.application.util.StringConstants;

/**
 * Created by ls on 16.01.14.
 */

public class PartnerPointSearchCriterionByCashlessPayments implements SearchCriterion<PartnerPoint>, Serializable {
    @Override
    public boolean meetCriterion(PartnerPoint partnerPoint) {
        MoreComparableString paymentMethods = new MoreComparableString(partnerPoint.paymentMethods);
        for (String each : StringConstants.cashlessPaymentMethods) {
            if (paymentMethods.containsIgnoreCase(each)) {
                return true;
            }
        }
        return false;
    }
}
