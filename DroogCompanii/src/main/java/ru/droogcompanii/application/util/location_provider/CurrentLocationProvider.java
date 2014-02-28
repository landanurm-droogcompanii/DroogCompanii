package ru.droogcompanii.application.util.location_provider;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import java.io.Serializable;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.util.LogUtils;
import ru.droogcompanii.application.util.Objects;

/**
 * Created by ls on 17.01.14.
 */
public class CurrentLocationProvider implements BaseLocationProvider, Serializable {

    private static Location currentLocation = null;

    private static class LocationListenerImpl implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            // skip
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

    @Override
    public Location getBaseLocation() {
        return CurrentLocationProvider.get();
    }

    public static Location get() {
        // This method might return <null>
        updateCurrentLocation();
        return currentLocation;
    }

    private static void updateCurrentLocation() {
        try {
            currentLocation = getLastKnownLocation();
        } catch (Throwable e) {
            LogUtils.debug(e.getMessage());
        }
    }

    private static Location getLastKnownLocation() {
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
        return lastKnownLocation;
    }

    private static LocationManager prepareLocationManager() {
        Context appContext = DroogCompaniiApplication.getContext();
        return (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
    }

    private static void requestLocationUpdates(final LocationManager locationManager, final String locationProvider) {
        Runnable requestLocationUpdates = new Runnable() {
            @Override
            public void run() {
            }
        };
        if (isUIThread()) {
        } else {
            Looper.prepare();
            locationManager.requestLocationUpdates(
                    locationProvider, MIN_TIME, MIN_DISTANCE, new LocationListenerImpl(), Looper.myLooper()
            );
            Looper.loop();
        }
    }

    private static boolean isUIThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
