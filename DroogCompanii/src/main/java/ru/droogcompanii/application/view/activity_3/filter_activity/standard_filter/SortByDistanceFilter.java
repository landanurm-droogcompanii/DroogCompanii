package ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter;

import android.location.Location;

import java.io.Serializable;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.view.activity_3.filter_activity.filter.SortingFilter;

/**
 * Created by ls on 16.01.14.
 */
class SortByDistanceFilter extends SortingFilter<PartnerPoint> implements Serializable {

    static interface BaseLocationProvider {
        Location getBaseLocation();
    }

    private final BaseLocationProvider baseLocationProvider;

    public SortByDistanceFilter(BaseLocationProvider baseLocationProvider) {
        this.baseLocationProvider = baseLocationProvider;
    }

    @Override
    public int compare(PartnerPoint partnerPoint1, PartnerPoint partnerPoint2) {
        Location baseLocation = baseLocationProvider.getBaseLocation();
        if (baseLocation == null) {
            return 0;
        }
        float d1 = baseLocation.distanceTo(locationFrom(partnerPoint1));
        float d2 = baseLocation.distanceTo(locationFrom(partnerPoint2));
        return Float.compare(d1, d2);
    }

    private static Location locationFrom(PartnerPoint partnerPoint) {
        Location location = new Location(partnerPoint.title);
        location.setLatitude(partnerPoint.latitude);
        location.setLongitude(partnerPoint.longitude);
        return location;
    }
}
