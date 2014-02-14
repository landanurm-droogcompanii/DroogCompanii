package ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner;

import android.location.Location;

import java.io.Serializable;
import java.util.Comparator;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.location_provider.BaseLocationProvider;
import ru.droogcompanii.application.util.MinMax;

/**
 * Created by ls on 16.01.14.
 */
public class PartnerComparatorByDistance implements Comparator<Partner>, Serializable {
    private final BaseLocationProvider baseLocationProvider;

    public PartnerComparatorByDistance(BaseLocationProvider baseLocationProvider) {
        this.baseLocationProvider = baseLocationProvider;
    }

    @Override
    public int compare(Partner partner1, Partner partner2) {
        Location baseLocation = baseLocationProvider.getBaseLocation();
        if (baseLocation == null) {
            return 0;
        }
        Float d1 = minDistanceFromPartnerToLocation(partner1, baseLocation);
        Float d2 = minDistanceFromPartnerToLocation(partner2, baseLocation);
        return d1.compareTo(d2);
    }

    private static Float minDistanceFromPartnerToLocation(Partner partner, Location location) {
        MinMax<Float> minMax = new MinMax<Float>();
        minMax.add(Float.MAX_VALUE);
        for (PartnerPoint partnerPoint : PartnerPointsProviderWithoutContext.getPointsOf(partner)) {
            minMax.add(partnerPoint.distanceTo(location));
        }
        return minMax.min();
    }
}
