package ru.droogcompanii.application;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import ru.droogcompanii.application.util.location.LocationConverter;

/**
 * Created by ls on 29.01.14.
 */
public class DroogCompaniiSettings {

    private static final double LATITUDE_OF_KAZAN = 55.79966;
    private static final double LONGITUDE_OF_KAZAN = 49.11053;
    private static final LatLng POSITION_OF_KAZAN = new LatLng(LATITUDE_OF_KAZAN, LONGITUDE_OF_KAZAN);

    private static final LatLng DEFAULT_POSITION = POSITION_OF_KAZAN;

    private static final int DEFAULT_ZOOM_LEVEL = 14;


    public static LatLng getDefaultBasePosition() {
        return DEFAULT_POSITION;
    }

    public static Location getDefaultBaseLocation() {
        return LocationConverter.fromLatLng("Default", DEFAULT_POSITION);
    }

    public static int getDefaultZoom() {
        return DEFAULT_ZOOM_LEVEL;
    }
}
