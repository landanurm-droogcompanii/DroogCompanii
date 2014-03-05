package ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner_point;

import android.location.Location;

import java.util.Comparator;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.InitializedByLocationProvider;
import ru.droogcompanii.application.util.BaseLocationProvider;

/**
 * Created by ls on 16.01.14.
 */
public class PartnerPointComparatorByDistance
        extends InitializedByLocationProvider
        implements Comparator<PartnerPoint> {

    public PartnerPointComparatorByDistance(BaseLocationProvider baseLocationProvider) {
        super(baseLocationProvider);
    }

    @Override
    public int compare(PartnerPoint partnerPoint1, PartnerPoint partnerPoint2) {
        Location baseLocation = super.getLocation();
        Float d1 = partnerPoint1.distanceTo(baseLocation);
        Float d2 = partnerPoint2.distanceTo(baseLocation);
        return d1.compareTo(d2);
    }
}
