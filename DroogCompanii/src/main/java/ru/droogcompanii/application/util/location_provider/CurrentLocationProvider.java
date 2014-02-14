package ru.droogcompanii.application.util.location_provider;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.io.Serializable;

import ru.droogcompanii.application.DroogCompaniiApplication;

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
        LocationManager locationManager = prepareLocationManager();
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        String enabledLocationProvider = LocationManager.GPS_PROVIDER;
        if (lastKnownLocation == null) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastKnownLocation == null) {
                return;
            }
            enabledLocationProvider = LocationManager.NETWORK_PROVIDER;
        }
        if (!lastKnownLocation.equals(currentLocation)) {
            LocationListener locationListener = new LocationListenerImpl();
            locationManager.requestLocationUpdates(
                    enabledLocationProvider, MIN_TIME, MIN_DISTANCE, locationListener
            );
        }
        currentLocation = lastKnownLocation;
    }

    private static LocationManager prepareLocationManager() {
        Context appContext = DroogCompaniiApplication.getContext();
        return (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
    }
}
