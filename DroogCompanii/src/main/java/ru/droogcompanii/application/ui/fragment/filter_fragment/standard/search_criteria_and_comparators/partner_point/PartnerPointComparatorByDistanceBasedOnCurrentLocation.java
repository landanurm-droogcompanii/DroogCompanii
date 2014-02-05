package ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators.partner_point;

import ru.droogcompanii.application.util.CurrentLocationProvider;

/**
 * Created by ls on 17.01.14.
 */
public class PartnerPointComparatorByDistanceBasedOnCurrentLocation extends PartnerPointComparatorByDistance {

    public PartnerPointComparatorByDistanceBasedOnCurrentLocation() {
        super(new CurrentLocationProvider());
    }
}
