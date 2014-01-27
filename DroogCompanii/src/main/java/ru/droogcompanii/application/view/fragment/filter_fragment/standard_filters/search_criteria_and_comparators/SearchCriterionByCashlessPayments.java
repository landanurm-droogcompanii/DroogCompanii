package ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.search_criteria_and_comparators;

import java.io.Serializable;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.util.DroogCompaniiStringConstants;
import ru.droogcompanii.application.util.MoreComparableString;

/**
 * Created by ls on 16.01.14.
 */

public class SearchCriterionByCashlessPayments
        implements SearchCriterion<PartnerPoint>, Serializable {
    @Override
    public boolean meetCriterion(PartnerPoint partnerPoint) {
        MoreComparableString paymentMethods = new MoreComparableString(partnerPoint.paymentMethods);
        for (String each : DroogCompaniiStringConstants.cashlessPaymentMethods) {
            if (paymentMethods.containsIgnoreCase(each)) {
                return true;
            }
        }
        return false;
    }
}
