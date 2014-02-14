package ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner;

import ru.droogcompanii.application.util.location_provider.SettingBaseLocationProvider;

/**
 * Created by ls on 17.01.14.
 */
public class PartnerComparatorByDistanceBasedOnCurrentLocation extends PartnerComparatorByDistance {

    public PartnerComparatorByDistanceBasedOnCurrentLocation() {
        super(new SettingBaseLocationProvider());
    }
}
