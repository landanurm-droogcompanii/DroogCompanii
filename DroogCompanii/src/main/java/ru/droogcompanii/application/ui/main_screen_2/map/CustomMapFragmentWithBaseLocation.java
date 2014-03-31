package ru.droogcompanii.application.ui.main_screen_2.map;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;

import ru.droogcompanii.application.util.location.ActualBaseLocationProvider;
import ru.droogcompanii.application.util.location.CurrentLocationUtils;
import ru.droogcompanii.application.util.location.CustomBaseLocationUtils;
import ru.droogcompanii.application.util.location.LocationStateListeners;

/**
 * Created by ls on 24.03.14.
 */
public class CustomMapFragmentWithBaseLocation extends CustomMapFragment
                         implements LocationStateListeners.LocationStateListener,
                                    GoogleMap.OnMapLongClickListener {


    public static interface Callbacks {
        void onCustomBaseLocationIsSet();
        void onCustomBaseLocationIsDismissed();
        void onNeedToEnableLocationService();
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

        @Override
        public void onNeedToEnableLocationService() {
            // do nothing
        }
    };


    private Callbacks callbacks;
    private LocationSource.OnLocationChangedListener onLocationChangedListener;



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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        NotifierAboutBaseLocationChanges.notify(getActivity());

        callbacks.onCustomBaseLocationIsSet();
    }


    public void dismissCustomBaseLocation() {
        CustomBaseLocationUtils.dismissBasePosition();
        NotifierAboutBaseLocationChanges.notify(getActivity());

        callbacks.onCustomBaseLocationIsDismissed();
    }

    @Override
    public void onCurrentAndCustomLocationsAreNotAvailable() {
        callbacks.onNeedToEnableLocationService();
    }

}
