package ru.droogcompanii.application;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import ru.droogcompanii.application.ui.util.LocationBuilder;

/**
 * Created by ls on 29.01.14.
 */
public class DroogCompaniiSettings {

    private static final double LATITUDE_OF_KAZAN = 55.79966;
    private static final double LONGITUDE_OF_KAZAN = 49.11053;

    private static final int DEFAULT_ZOOM_LEVEL = 14;


    public static LatLng getDefaultBasePosition() {
        return getPositionOfKazan();
    }

    private static LatLng getPositionOfKazan() {
        return new LatLng(LATITUDE_OF_KAZAN, LONGITUDE_OF_KAZAN);
    }

    public static Location getDefaultBaseLocation() {
        return getLocationOfKazan();
    }

    private static Location getLocationOfKazan() {
        return LocationBuilder.fromLatitudeLongitude("Kazan", LATITUDE_OF_KAZAN, LONGITUDE_OF_KAZAN);
    }

    public static int getDefaultZoom() {
        return DEFAULT_ZOOM_LEVEL;
    }
}
