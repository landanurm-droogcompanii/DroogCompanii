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

import ru.droogcompanii.application.DroogCompaniiApplication;

/**
 * Created by ls on 17.01.14.
 */
public class CurrentLocationProvider implements BaseLocationProvider, Serializable {

    public static final LocationSource.OnLocationChangedListener
            DUMMY_ON_CURRENT_LOCATION_CHANGED_LISTENER = new LocationSource.OnLocationChangedListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // do nothing
                }
            };

    private static WeakReferenceWrapper<LocationSource.OnLocationChangedListener>
            listenerWrapper = WeakReferenceWrapper.from(DUMMY_ON_CURRENT_LOCATION_CHANGED_LISTENER);

    private static Optional<Location> currentLocation = Optional.absent();

    private static class LocationListenerImpl implements LocationListener {

        @Override
        public void onLocationChanged(final Location location) {
            currentLocation = Optional.fromNullable(location);
            notifyListenerIfItExists();
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


    public static void setOnLocationChangedListener(LocationSource.OnLocationChangedListener listener) {
        listenerWrapper = WeakReferenceWrapper.from(listener);
    }

    @Override
    public Optional<Location> getBaseLocation() {
        return CurrentLocationProvider.get();
    }

    public static Optional<Location> get() {
        updateCurrentLocation();
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
        notifyListenerIfItExists();
    }

    private static void notifyListenerIfItExists() {
        listenerWrapper.handleIfExist(new WeakReferenceWrapper.Handler<LocationSource.OnLocationChangedListener>() {
            @Override
            public void handle(LocationSource.OnLocationChangedListener listener) {
                listener.onLocationChanged(currentLocation.orNull());
            }
        });
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
        Context appContext = DroogCompaniiApplication.getContext();
        return (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
    }
}
