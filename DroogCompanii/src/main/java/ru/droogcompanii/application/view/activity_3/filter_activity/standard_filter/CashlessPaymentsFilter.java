package ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter;

import java.io.Serializable;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.DroogCompaniiStringConstants;
import ru.droogcompanii.application.view.activity_3.filter_activity.filter.SearchFilter;

/**
 * Created by ls on 16.01.14.
 */

class CashlessPaymentsFilter extends SearchFilter<PartnerPoint> implements Serializable {
    @Override
    public boolean meetCriteria(PartnerPoint partnerPoint) {
        return partnerPoint.paymentMethods.contains(DroogCompaniiStringConstants.cashlessPayments);
    }
}
