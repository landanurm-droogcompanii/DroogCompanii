package ru.droogcompanii.application;

import android.location.Location;

import ru.droogcompanii.application.util.CurrentLocationProvider;

/**
 * Created by ls on 29.01.14.
 */
public class DroogCompaniiSettings {
    private static final double LATITUDE_OF_DEFAULT_BASE_LOCATION = 51.2419;
    private static final double LONGITUDE_OF_DEFAULT_BASE_LOCATION = 53.6579;

    public static Location getBaseLocation() {
        Location baseLocation = CurrentLocationProvider.get();
        return baseLocation != null ? baseLocation : getDefaultBaseLocation();
    }

    private static Location getDefaultBaseLocation() {
        Location location = new Location("defaultLocation");
        location.setLatitude(LATITUDE_OF_DEFAULT_BASE_LOCATION);
        location.setLongitude(LONGITUDE_OF_DEFAULT_BASE_LOCATION);
        return location;
    }
}
