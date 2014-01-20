package ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.filters;

import java.io.Serializable;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.DroogCompaniiStringConstants;
import ru.droogcompanii.application.data.searchable_sortable.filter.SearchFilter;
import ru.droogcompanii.application.util.MoreComparableString;

/**
 * Created by ls on 16.01.14.
 */

public class CashlessPaymentsFilter extends SearchFilter<PartnerPoint> implements Serializable {
    @Override
    public boolean meetCriteria(PartnerPoint partnerPoint) {
        MoreComparableString paymentMethods = new MoreComparableString(partnerPoint.paymentMethods);
        for (String each : DroogCompaniiStringConstants.cashlessPaymentMethods) {
            if (paymentMethods.containsIgnoreCase(each)) {
                return true;
            }
        }
        return false;
    }
}
