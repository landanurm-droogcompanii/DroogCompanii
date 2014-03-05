package ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner_point;

import android.location.Location;

import java.io.Serializable;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.util.BaseLocationProvider;

/**
 * Created by ls on 14.02.14.
 */
public class PartnerPointSearchClosestPointsCriterion
        implements SearchCriterion<PartnerPoint>, Serializable {
    private final float radiusInMeters;
    private final BaseLocationProvider baseLocationProvider;

    public PartnerPointSearchClosestPointsCriterion(
            BaseLocationProvider baseLocationProvider, float radiusInMeters) {
        this.baseLocationProvider = baseLocationProvider;
        this.radiusInMeters = radiusInMeters;
    }

    @Override
    public boolean meetCriterion(PartnerPoint partnerPoint) {
        Location baseLocation = baseLocationProvider.getBaseLocation();
        float distanceInMeters = partnerPoint.distanceTo(baseLocation);
        return (distanceInMeters <= radiusInMeters);
    }
}
