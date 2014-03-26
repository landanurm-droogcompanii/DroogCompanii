package ru.droogcompanii.application.ui.main_screen_2.map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.util.activity.ActivityWithNavigationDrawer;
import ru.droogcompanii.application.util.location.LocationConverter;
import ru.droogcompanii.application.util.LogUtils;
import ru.droogcompanii.application.util.OnEachHandler;

/**
 * Created by ls on 24.03.14.
 */
public abstract class MapActivityLocationProvider extends ActivityWithNavigationDrawer
                                implements GooglePlayServicesClient.ConnectionCallbacks,
                                           GooglePlayServicesClient.OnConnectionFailedListener{

    public static interface LocationListener {
        void onLocationChanged(Location location);
        void onConnected();
        void onDisconnected();
        void onConnectionFailed();
    }


    public static class ErrorDialogFragment extends DialogFragment {
        private Dialog mDialog;
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    protected LocationClient locationClient;
    protected final List<LocationListener> listeners = new ArrayList<LocationListener>();

    public void addLocationListener(LocationListener listener) {
        listeners.add(listener);
    }

    public void removeLocationListener(LocationListener listener) {
        listeners.remove(listener);
    }

    protected void forEachListener(OnEachHandler<LocationListener> onEachHandler) {
        for (LocationListener each : listeners) {
            onEachHandler.onEach(each);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
                if (resultCode == Activity.RESULT_OK) {
                    locationClient.connect();
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean isServicesConnected() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            LogUtils.debug("Google Play services is available.");
            return true;
        }
        showErrorDialog(resultCode);
        return false;
    }

    private void showErrorDialog(int resultCode) {
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                resultCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
        if (errorDialog != null) {
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();
            errorFragment.setDialog(errorDialog);
            errorFragment.show(getSupportFragmentManager(), "Location Updates");
        }
    }


    @Override
    public void onConnected(Bundle dataBundle) {
        LogUtils.debug("Connected");
        forEachListener(new OnEachHandler<LocationListener>() {
            @Override
            public void onEach(LocationListener each) {
                each.onConnected();
            }
        });
    }

    @Override
    public void onDisconnected() {
        LogUtils.debug("Disconnected");
        forEachListener(new OnEachHandler<LocationListener>() {
            @Override
            public void onEach(LocationListener each) {
                each.onDisconnected();
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        LogUtils.debug("Connection failed");
        handleFailedConnectionResult(connectionResult);
        forEachListener(new OnEachHandler<LocationListener>() {
            @Override
            public void onEach(LocationListener each) {
                each.onConnectionFailed();
            }
        });
    }

    private void handleFailedConnectionResult(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                LogUtils.exception(e);
            }
        } else {
            showErrorDialog(connectionResult.getErrorCode());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationClient = new LocationClient(this, this, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationClient.connect();
    }

    @Override
    protected void onStop() {
        locationClient.disconnect();
        super.onStop();
    }

    public Location getLastLocation() {
        return locationClient.getLastLocation();
    }

    public LatLng getPositionOfLastLocation() {
        return LocationConverter.toLatLng(locationClient.getLastLocation());
    }

}
