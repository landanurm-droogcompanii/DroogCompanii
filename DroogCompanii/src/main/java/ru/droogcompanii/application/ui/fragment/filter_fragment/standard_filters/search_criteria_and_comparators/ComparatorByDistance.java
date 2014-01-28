package ru.droogcompanii.application.ui.fragment.filter_fragment.standard_filters.search_criteria_and_comparators;

import android.location.Location;

import java.io.Serializable;
import java.util.Comparator;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 16.01.14.
 */
public class ComparatorByDistance implements Comparator<PartnerPoint>, Serializable {

    public static interface BaseLocationProvider {
        Location getBaseLocation();
    }

    private final BaseLocationProvider baseLocationProvider;

    public ComparatorByDistance(BaseLocationProvider baseLocationProvider) {
        this.baseLocationProvider = baseLocationProvider;
    }

    @Override
    public int compare(PartnerPoint partnerPoint1, PartnerPoint partnerPoint2) {
        Location baseLocation = baseLocationProvider.getBaseLocation();
        if (baseLocation == null) {
            return 0;
        }
        Float d1 = baseLocation.distanceTo(locationFrom(partnerPoint1));
        Float d2 = baseLocation.distanceTo(locationFrom(partnerPoint2));
        return d1.compareTo(d2);
    }

    private static Location locationFrom(PartnerPoint partnerPoint) {
        Location location = new Location(partnerPoint.title);
        location.setLatitude(partnerPoint.latitude);
        location.setLongitude(partnerPoint.longitude);
        return location;
    }
}
