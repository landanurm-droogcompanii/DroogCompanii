package ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner_point;

import ru.droogcompanii.application.ui.util.SettingBaseLocationProvider;

/**
 * Created by ls on 17.01.14.
 */
public class PartnerPointComparatorByDistanceBasedOnCurrentLocation extends PartnerPointComparatorByDistance {

    public PartnerPointComparatorByDistanceBasedOnCurrentLocation() {
        super(new SettingBaseLocationProvider());
    }
}
