package ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner_point;

import android.location.Location;

import java.io.Serializable;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.util.location_provider.BaseLocationProvider;

/**
 * Created by ls on 14.02.14.
 */
public class PartnerPointSearchClosestPointsCriterion
        implements SearchCriterion<PartnerPoint>, Serializable {

    private static final boolean IS_MEET_CRITERION_IF_BASE_LOCATION_IS_NOT_AVAILABLE = true;

    private final BaseLocationProvider baseLocationProvider;
    private final float radiusInMeters;

    public PartnerPointSearchClosestPointsCriterion(
            BaseLocationProvider baseLocationProvider, float radiusInMeters) {
        this.baseLocationProvider = baseLocationProvider;
        this.radiusInMeters = radiusInMeters;
    }

    @Override
    public boolean meetCriterion(PartnerPoint partnerPoint) {
        Location location = baseLocationProvider.getBaseLocation();
        float distanceInMeters = partnerPoint.distanceTo(location);
        return (distanceInMeters <= radiusInMeters);
    }
}
