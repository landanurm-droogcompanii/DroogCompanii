package ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter;

import java.io.Serializable;

import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.view.activity_3.filter_activity.filter.SortingFilter;

/**
 * Created by ls on 16.01.14.
 */
class SortByDistanceFilter extends SortingFilter<PartnerPoint> implements Serializable {
    @Override
    public int compare(PartnerPoint partnerPoint1, PartnerPoint partnerPoint2) {
        // TODO: необходимо получать позиции точки, по расстоянию к которой нужно сортировать точки
        throw new RuntimeException("Not Implemented Yet");
    }
}
