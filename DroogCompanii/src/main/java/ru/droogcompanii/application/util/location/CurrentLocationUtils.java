package ru.droogcompanii.application.util.location;

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
import java.util.Collection;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.util.BaseLocationProvider;
import ru.droogcompanii.application.util.Holder;
import ru.droogcompanii.application.util.LogUtils;
import ru.droogcompanii.application.util.Objects;
import ru.droogcompanii.application.util.WeakReferenceWrapper;

/**
 * Created by ls on 17.01.14.
 */
public class CurrentLocationUtils implements BaseLocationProvider, Serializable {

    private static Collection<WeakReferenceWrapper<LocationSource.OnLocationChangedListener>>
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


    public static void addOnLocationChangedListener(LocationSource.OnLocationChangedListener listener) {
        removeOnLocationChangedListener(listener);
        listenerWrappers.add(WeakReferenceWrapper.from(listener));
    }

    public static void removeOnLocationChangedListener(final LocationSource.OnLocationChangedListener listenerToRemove) {
        final Holder<Boolean> isFound = Holder.from(false);
        for (final WeakReferenceWrapper<LocationSource.OnLocationChangedListener> eachWrapper : listenerWrappers) {
            eachWrapper.handleIfExist(new WeakReferenceWrapper.Handler<LocationSource.OnLocationChangedListener>() {
                @Override
                public void handle(LocationSource.OnLocationChangedListener eachListener) {
                    if (Objects.equals(eachListener, listenerToRemove)) {
                        isFound.value = true;
                    }
                }
            });
            if (isFound.value) {
                listenerWrappers.remove(eachWrapper);
                break;
            }
        }
    }

    @Override
    public Location getBaseLocation() {
        return ActualBaseLocationProvider.getActualBaseLocation();
    }

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
        notifyListeners();
    }

    public static void notifyListeners() {
        final Location actualLocation = ActualBaseLocationProvider.getActualBaseLocation();
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
