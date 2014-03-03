package ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner;

import android.location.Location;

import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.Comparator;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.BaseLocationProvider;
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
        Optional<Location> baseLocation = baseLocationProvider.getBaseLocation();

        if (!baseLocation.isPresent()) {
            return 0;
        }
        Float d1 = minDistanceFromPartnerToLocation(partner1, baseLocation.get());
        Float d2 = minDistanceFromPartnerToLocation(partner2, baseLocation.get());
        int result = d1.compareTo(d2);

        return result;
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
