package ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner_point;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.Serializable;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.util.location.ActualBaseLocationProvider;

/**
 * Created by ls on 14.02.14.
 */
public class PartnerPointSearchClosestPointsCriterion
        implements SearchCriterion<PartnerPoint>, Serializable {
    private final double maxRadiusInMeters;

    public PartnerPointSearchClosestPointsCriterion(double maxRadiusInMeters) {
        this.maxRadiusInMeters = maxRadiusInMeters;
    }

    @Override
    public boolean meetCriterion(PartnerPoint partnerPoint) {
        LatLng basePosition = ActualBaseLocationProvider.getPositionOfActualBaseLocation();
        double distanceInMeters = SphericalUtil.computeDistanceBetween(partnerPoint.getPosition(), basePosition);
        return (distanceInMeters <= maxRadiusInMeters);
    }
}
