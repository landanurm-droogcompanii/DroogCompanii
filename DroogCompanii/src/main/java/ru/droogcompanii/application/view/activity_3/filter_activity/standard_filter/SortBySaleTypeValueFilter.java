package ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter;


import java.io.Serializable;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.view.activity_3.filter_activity.filter.SortingFilter;

/**
 * Created by ls on 16.01.14.
 */

class SortBySaleTypeValueFilter extends SortingFilter<PartnerPoint> implements Serializable {
    @Override
    public int compare(PartnerPoint partnerPoint, PartnerPoint partnerPoint2) {
        // TODO: Сервер должен передавать размер скидки
        throw new RuntimeException("Not Implemented Yet");
    }
}
