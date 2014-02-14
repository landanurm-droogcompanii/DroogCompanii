package ru.droogcompanii.application;

import android.location.Location;

import ru.droogcompanii.application.util.location_provider.CurrentLocationProvider;

/**
 * Created by ls on 29.01.14.
 */
public class DroogCompaniiSettings {
    private static final double LATITUDE_OF_KAZAN = 55.78856;
    private static final double LONGITUDE_OF_KAZAN = 49.12163;

    public static Location getBaseLocation() {
        Location baseLocation = CurrentLocationProvider.get();
        return baseLocation != null ? baseLocation : getDefaultBaseLocation();
    }

    private static Location getDefaultBaseLocation() {
        return getLocationOfKazan();
    }

    private static Location getLocationOfKazan() {
        Location location = new Location("Kazan");
        location.setLatitude(LATITUDE_OF_KAZAN);
        location.setLongitude(LONGITUDE_OF_KAZAN);
        return location;
    }
}
