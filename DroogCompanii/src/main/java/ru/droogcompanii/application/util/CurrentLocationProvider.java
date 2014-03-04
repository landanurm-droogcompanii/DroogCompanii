package ru.droogcompanii.application.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import com.google.android.gms.maps.LocationSource;
import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.DroogCompaniiSettings;

/**
 * Created by ls on 17.01.14.
 */
public class CurrentLocationProvider implements BaseLocationProvider, Serializable {

    private static List<WeakReferenceWrapper<LocationSource.OnLocationChangedListener>>
            listenerWrappers = new ArrayList<WeakReferenceWrapper<LocationSource.OnLocationChangedListener>>();

    private static Optional<Location> currentLocation = Optional.absent();


    private static class LocationListenerImpl implements LocationListener {

        @Override
        public void onLocationChanged(final Location location) {
            currentLocation = Optional.fromNullable(location);
            notifyListeners();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (status == LocationProvider.AVAILABLE) {
                CurrentLocationProvider.updateCurrentLocation();
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


    public static void addOnLocationChangedListener(LocationSource.OnLocationChangedListener listener) {
        listenerWrappers.add(WeakReferenceWrapper.from(listener));
    }

    public static void removeOnLocationChangedListener(LocationSource.OnLocationChangedListener listener) {
        listenerWrappers.remove(WeakReferenceWrapper.from(listener));
    }

    @Override
    public Location getBaseLocation() {
        return getCurrentOrDefaultLocation();
    }

    public static Location getUpdatedCurrentOrDefaultLocation() {
        updateCurrentLocation();
        return getCurrentOrDefaultLocation();
    }

    public static Location getCurrentOrDefaultLocation() {
        Location defaultLocation = DroogCompaniiSettings.getDefaultBaseLocation();
        return currentLocation.or(defaultLocation);
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
        notifyListeners();
    }

    private static void notifyListeners() {
        final Location actualLocation = getCurrentOrDefaultLocation();
        for (WeakReferenceWrapper<LocationSource.OnLocationChangedListener> listenerWrapper : listenerWrappers) {
            listenerWrapper.handleIfExist(new WeakReferenceWrapper.Handler<LocationSource.OnLocationChangedListener>() {
                @Override
                public void handle(LocationSource.OnLocationChangedListener listener) {
                    listener.onLocationChanged(actualLocation);
                }
            });
        }
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
