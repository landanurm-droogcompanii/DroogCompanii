package ru.droogcompanii.application.ui.util.location;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.common.base.Optional;

import java.io.Serializable;

import ru.droogcompanii.application.DroogCompaniiSettings;
import ru.droogcompanii.application.util.BaseLocationProvider;

/**
 * Created by ls on 14.02.14.
 */
public class ActualBaseLocationProvider implements BaseLocationProvider, Serializable {

    public static LatLng getPositionOfActualBaseLocation() {
        Location actualLocation = getActualBaseLocation();
        return LocationConverter.toLatLng(actualLocation);
    }

    public static Location getActualBaseLocation() {
        if (CustomBaseLocationUtils.isBasePositionSet()) {
            return CustomBaseLocationUtils.getBaseLocation();
        }
        Optional<Location> currentLocation = CurrentLocationUtils.getCurrentLocation();
        if (currentLocation.isPresent()) {
            return currentLocation.get();
        }
        return DroogCompaniiSettings.getDefaultBaseLocation();
    }

    @Override
    public Location getBaseLocation() {
        return ActualBaseLocationProvider.getActualBaseLocation();
    }
}
