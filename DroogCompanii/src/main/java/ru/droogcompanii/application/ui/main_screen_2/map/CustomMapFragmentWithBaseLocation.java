package ru.droogcompanii.application.ui.main_screen_2.map;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;

import ru.droogcompanii.application.ui.fragment.partner_points_map.CustomMapFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map.NotifierAboutBaseMapLocationChanges;
import ru.droogcompanii.application.util.StateManager;
import ru.droogcompanii.application.util.location.ActualBaseLocationProvider;
import ru.droogcompanii.application.util.location.CurrentLocationUtils;
import ru.droogcompanii.application.util.location.CustomBaseLocationUtils;
import ru.droogcompanii.application.util.location.LocationStateListeners;
import ru.droogcompanii.application.util.view.MessageBar;
import ru.droogcompanii.application.util.view.ToastUtils;

/**
 * Created by ls on 24.03.14.
 */
public class CustomMapFragmentWithBaseLocation extends CustomMapFragment
                         implements LocationStateListeners.LocationStateListener,
                                    GoogleMap.OnMapLongClickListener {


    public static interface Callbacks {
        void onCustomBaseLocationIsSet();
        void onCustomBaseLocationIsDismissed();
    }


    private static final LocationSource.OnLocationChangedListener
            DUMMY_ON_LOCATION_CHANGED_LISTENER = new LocationSource.OnLocationChangedListener() {
        @Override
        public void onLocationChanged(Location location) {
            // do nothing
        }
    };

    private static final Callbacks DUMMY_CALLBACKS = new Callbacks() {
        @Override
        public void onCustomBaseLocationIsSet() {
            // do nothing
        }

        @Override
        public void onCustomBaseLocationIsDismissed() {
            // do nothing
        }
    };


    private static class Key {
        public static final String STATE_OF_MESSAGE_BAR = "STATE_OF_MESSAGE_BAR";
    }


    private Callbacks callbacks;
    private LocationSource.OnLocationChangedListener onLocationChangedListener;

    private MessageBar mMessageBar;

    private final StateManager STATE_MANAGER = new StateManager() {

        @Override
        public void initStateByDefault() {
            initMessageBar();
        }

        private void initMessageBar() {
            mMessageBar = new MessageBar(getActivity());
            mMessageBar.setOnClickListener(new MessageBar.OnMessageClickListener() {
                @Override
                public void onMessageClick(Parcelable token) {
                    onNotifierAboutLocationClicked();
                }
            });
        }

        @Override
        public void restoreState(Bundle savedInstanceState) {
            initMessageBar();
            Bundle stateOfMessageBar = savedInstanceState.getBundle(Key.STATE_OF_MESSAGE_BAR);
            mMessageBar.onRestoreInstanceState(stateOfMessageBar);
        }

        @Override
        public void saveState(Bundle outState) {
            outState.putBundle(Key.STATE_OF_MESSAGE_BAR, mMessageBar.onSaveInstanceState());
        }
    };


    public CustomMapFragmentWithBaseLocation() {
        onLocationChangedListener = DUMMY_ON_LOCATION_CHANGED_LISTENER;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        callbacks = DUMMY_CALLBACKS;
        onLocationChangedListener = DUMMY_ON_LOCATION_CHANGED_LISTENER;
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        STATE_MANAGER.saveState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        STATE_MANAGER.initState(savedInstanceState);

        initCurrentLocation();
        getGoogleMap().setOnMapLongClickListener(this);
    }


    protected void initCurrentLocation() {
        GoogleMap googleMap = getGoogleMap();
        if (googleMap == null) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                CustomMapFragmentWithBaseLocation.this.onLocationChangedListener = onLocationChangedListener;
                LocationStateListeners.notifyListenersAboutLocationChange();
            }

            @Override
            public void deactivate() {
                CustomMapFragmentWithBaseLocation.this.onLocationChangedListener = DUMMY_ON_LOCATION_CHANGED_LISTENER;
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        onLocationChangedListener.onLocationChanged(ActualBaseLocationProvider.getActualBaseLocation());
    }

    @Override
    public void onResume() {
        super.onResume();
        LocationStateListeners.addListener(this);
        CurrentLocationUtils.updateCurrentLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocationStateListeners.removeListener(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (isAbleToChangeBaseLocationByLongClickOnMap()) {
            setCustomBaseLocation(latLng);
        }
    }


    protected boolean isAbleToChangeBaseLocationByLongClickOnMap() {
        return true;
    }


    private void setCustomBaseLocation(LatLng latLng) {
        CustomBaseLocationUtils.updateBasePosition(latLng);
        NotifierAboutBaseMapLocationChanges.notify(getActivity());

        callbacks.onCustomBaseLocationIsSet();
    }


    public void dismissCustomBaseLocation() {
        CustomBaseLocationUtils.dismissBasePosition();
        NotifierAboutBaseMapLocationChanges.notify(getActivity());

        callbacks.onCustomBaseLocationIsDismissed();
    }

    @Override
    public void onCurrentAndCustomLocationsAreNotAvailable() {
        ToastUtils.LONG.show(getActivity(), "Location is not available. Please, turn on Location Service");
        mMessageBar.show("Location is not available. Please, turn on Location Service");
    }

    private void onNotifierAboutLocationClicked() {
        openLocationSettingsScreen();
    }

    private void openLocationSettingsScreen() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }
}
