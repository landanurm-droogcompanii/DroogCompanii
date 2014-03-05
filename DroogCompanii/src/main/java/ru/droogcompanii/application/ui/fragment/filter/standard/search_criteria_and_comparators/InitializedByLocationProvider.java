package ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators;

import android.location.Location;

import java.io.Serializable;

import ru.droogcompanii.application.util.BaseLocationProvider;

/**
 * Created by ls on 05.03.14.
 */
public class InitializedByLocationProvider implements Serializable {

    private final double latitude;
    private final double longitude;
    private final String provider;

    protected InitializedByLocationProvider(BaseLocationProvider baseLocationProvider) {
        Location location = baseLocationProvider.getBaseLocation();
        provider = location.getProvider();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    protected Location getLocation() {
        Location location = new Location(provider);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }
}
