package ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.Serializable;
import java.util.Comparator;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.MinMax;
import ru.droogcompanii.application.util.location.ActualBaseLocationProvider;

/**
 * Created by ls on 16.01.14.
 */
public class PartnerComparatorByDistance implements Comparator<Partner>, Serializable {

    @Override
    public int compare(Partner partner1, Partner partner2) {
        LatLng basePosition = ActualBaseLocationProvider.getPositionOfActualBaseLocation();
        Double d1 = minDistanceFromPartnerToBasePosition(partner1, basePosition);
        Double d2 = minDistanceFromPartnerToBasePosition(partner2, basePosition);
        return d1.compareTo(d2);
    }

    private static Double minDistanceFromPartnerToBasePosition(Partner partner, LatLng basePosition) {
        MinMax<Double> minMax = new MinMax<Double>();
        minMax.add(Double.MAX_VALUE);
        for (PartnerPoint partnerPoint : PartnerPointsProviderWithoutContext.getPointsOf(partner)) {
            double distance = SphericalUtil.computeDistanceBetween(partnerPoint.getPosition(), basePosition);
            minMax.add(distance);
        }
        return minMax.min();
    }
}
