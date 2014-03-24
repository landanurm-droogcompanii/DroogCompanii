package ru.droogcompanii.application.ui.util;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ls on 05.03.14.
 */
public class LocationConverter {

    public static LatLng toLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static Location fromLatLng(String provider, LatLng latLng) {
        return fromLatitudeLongitude(provider, latLng.latitude, latLng.longitude);
    }

    public static Location fromLatitudeLongitude(String provider, double latitude, double longitude) {
        Location location = new Location(provider);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }
}
