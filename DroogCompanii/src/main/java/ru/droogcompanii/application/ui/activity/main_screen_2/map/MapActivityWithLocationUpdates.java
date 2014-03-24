package ru.droogcompanii.application.ui.activity.main_screen_2.map;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import ru.droogcompanii.application.util.LogUtils;
import ru.droogcompanii.application.util.OnEachHandler;

/**
 * Created by ls on 24.03.14.
 */
public abstract class MapActivityWithLocationUpdates
        extends MapActivityLocationProvider implements LocationListener {

    private static class Key {
        public static final String UPDATES_REQUESTED = "KEY_UPDATES_REQUESTED";
    }

    private static final boolean DEFAULT_UPDATES_REQUESTED = true;

    private static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    private static final long UPDATE_INTERVAL = UPDATE_INTERVAL_IN_SECONDS * MILLISECONDS_PER_SECOND;
    private static final long FASTEST_INTERVAL = FASTEST_INTERVAL_IN_SECONDS * MILLISECONDS_PER_SECOND;

    private boolean mUpdatesRequested;
    private LocationRequest mLocationRequest;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUpdatesRequested = DEFAULT_UPDATES_REQUESTED;
        initLocationRequest();
        initSharedPreferences();
    }

    private void initLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
    }

    private void initSharedPreferences() {
        mPrefs = getSharedPreferences(getClass().getName() + "SharedPreferences", Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }

    @Override
    protected void onPause() {
        saveUpdatesRequested();
        super.onPause();
    }

    private void saveUpdatesRequested() {
        mEditor.putBoolean(Key.UPDATES_REQUESTED, mUpdatesRequested);
        mEditor.commit();
    }

    @Override
    protected void onResume() {
        restoreUpdatesRequested();
        super.onResume();
    }

    private void restoreUpdatesRequested() {
        if (mPrefs.contains(Key.UPDATES_REQUESTED)) {
            mUpdatesRequested = mPrefs.getBoolean(Key.UPDATES_REQUESTED, DEFAULT_UPDATES_REQUESTED);
        } else {
            mEditor.putBoolean(Key.UPDATES_REQUESTED, false);
            mEditor.commit();
        }
    }

    @Override
    public void onConnected(Bundle dataBundle) {
        super.onConnected(dataBundle);
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        if (mUpdatesRequested) {
            locationClient.requestLocationUpdates(mLocationRequest, this);
        }
    }

    @Override
    public void onLocationChanged(final Location location) {
        LogUtils.debug("onLocationChanged()");
        requestLocationUpdates();
        forEachListener(new OnEachHandler<LocationListener>() {
            @Override
            public void onEach(LocationListener each) {
                each.onLocationChanged(location);
            }
        });
    }

    public void turnOnLocationUpdates() {
        checkAbilityToTurnOnOffLocationUpdates();
        if (mUpdatesRequested) {
            return;
        }
        mUpdatesRequested = true;
        locationClient.requestLocationUpdates(mLocationRequest, this);
    }

    private void checkAbilityToTurnOnOffLocationUpdates() {
        /*
        if (isActivityResumed()) {
            throw new IllegalStateException(
                    "You able to turn on/off location updates only during activity resuming"
            );
        }
        */
    }

    public void turnOffLocationUpdates() {
        checkAbilityToTurnOnOffLocationUpdates();
        if (!mUpdatesRequested) {
            return;
        }
        mUpdatesRequested = false;
        locationClient.removeLocationUpdates(this);
    }

    public boolean isLocationClientConnected() {
        return (locationClient != null) && locationClient.isConnected();
    }


    @Override
    protected void onStop() {
        if (isLocationClientConnected()) {
            locationClient.removeLocationUpdates(this);
        }
        super.onStop();
    }
}
