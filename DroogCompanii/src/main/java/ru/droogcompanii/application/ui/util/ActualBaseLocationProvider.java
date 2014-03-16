package ru.droogcompanii.application.ui.util;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import ru.droogcompanii.application.DroogCompaniiSettings;
import ru.droogcompanii.application.util.BaseLocationProvider;

/**
 * Created by ls on 14.02.14.
 */
public class ActualBaseLocationProvider implements BaseLocationProvider, Serializable {

    public static LatLng getPositionOfActualBaseLocation() {
        Location actualLocation = getActualBaseLocation();
        return new LatLng(actualLocation.getLatitude(), actualLocation.getLongitude());
    }

    public static Location getActualBaseLocation() {
        if (CustomBaseLocationUtils.isBasePositionSet()) {
            return getCustomBaseLocation();
        }
        Location defaultLocation = DroogCompaniiSettings.getDefaultBaseLocation();
        return LocationUtils.getCurrentLocation().or(defaultLocation);
    }

    private static Location getCustomBaseLocation() {
        LatLng basePosition = CustomBaseLocationUtils.getBasePosition();
        return LocationBuilder.fromLatLng("custom", basePosition);
    }

    @Override
    public Location getBaseLocation() {
        return ActualBaseLocationProvider.getActualBaseLocation();
    }
}
