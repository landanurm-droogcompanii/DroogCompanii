package ru.droogcompanii.application.util.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import com.google.common.base.Optional;

import java.io.Serializable;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.util.LogUtils;
import ru.droogcompanii.application.util.Objects;

/**
 * Created by ls on 17.01.14.
 */
public class CurrentLocationUtils implements Serializable {

    private static Optional<Location> currentLocation = Optional.absent();


    private static class LocationListenerImpl implements LocationListener {

        @Override
        public void onLocationChanged(final Location location) {
            currentLocation = Optional.fromNullable(location);
            notifyListenersIfNeed();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (status == LocationProvider.AVAILABLE) {
                CurrentLocationUtils.updateCurrentLocation();
            }
        }

        @Override
        public void onProviderEnabled(String s) {
            // skip
        }

        @Override
        public void onProviderDisabled(String s) {
            // skip
        }
    }

    private static final long MIN_TIME = 5000L;
    private static final float MIN_DISTANCE = 10f;


    public static Optional<Location> getCurrentLocation() {
        return currentLocation;
    }

    public static void updateCurrentLocation() {
        try {
            tryUpdateCurrentLocation();
        } catch (Throwable e) {
            LogUtils.debug(e.getMessage());
        }
    }

    private static void tryUpdateCurrentLocation() {
        Optional<Location> lastKnownLocation = getLastKnownLocation();
        if (lastKnownLocation.isPresent()) {
            currentLocation = lastKnownLocation;
        }
        notifyListenersIfNeed();
    }

    private static void notifyListenersIfNeed() {
        if (CustomBaseLocationUtils.isBasePositionSet()) {
            return;
        }
        OnLocationChangedListeners.notifyListeners();
    }

    private static Optional<Location> getLastKnownLocation() {
        final LocationManager locationManager = prepareLocationManager();
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        String enabledLocationProvider = LocationManager.GPS_PROVIDER;
        if (lastKnownLocation == null) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            enabledLocationProvider = LocationManager.NETWORK_PROVIDER;
        }
        if (lastKnownLocation != null && !Objects.equals(lastKnownLocation, currentLocation)) {
            locationManager.requestLocationUpdates(
                    enabledLocationProvider, MIN_TIME, MIN_DISTANCE, new LocationListenerImpl()
            );
        }
        return Optional.fromNullable(lastKnownLocation);
    }

    private static LocationManager prepareLocationManager() {
        return (LocationManager) DroogCompaniiApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
    }
}
