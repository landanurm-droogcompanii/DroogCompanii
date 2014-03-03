package ru.droogcompanii.application;

import android.location.Location;

import com.google.common.base.Optional;

import ru.droogcompanii.application.util.CurrentLocationProvider;

/**
 * Created by ls on 29.01.14.
 */
public class DroogCompaniiSettings {

    private static final double LATITUDE_OF_KAZAN = 55.78856;
    private static final double LONGITUDE_OF_KAZAN = 49.12163;

    private static final int DEFAULT_ZOOM_LEVEL = 14;


    public static Location getBaseLocation() {
        Optional<Location> baseLocation = CurrentLocationProvider.get();
        return baseLocation.isPresent() ? baseLocation.get() : getDefaultBaseLocation();
    }

    public static Location getDefaultBaseLocation() {
        return getLocationOfKazan();
    }

    private static Location getLocationOfKazan() {
        Location location = new Location("Kazan");
        location.setLatitude(LATITUDE_OF_KAZAN + 0.0111);
        location.setLongitude(LONGITUDE_OF_KAZAN - 0.0111);
        return location;
    }

    public static int getDefaultZoom() {
        return DEFAULT_ZOOM_LEVEL;
    }
}
