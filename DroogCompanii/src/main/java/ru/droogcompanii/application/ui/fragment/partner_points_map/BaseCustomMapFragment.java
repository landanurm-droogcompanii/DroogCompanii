package ru.droogcompanii.application.ui.fragment.partner_points_map;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.DroogCompaniiSettings;
import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.util.FragmentAbleRunOnceOnResume;
import ru.droogcompanii.application.ui.util.ObserverOfViewWillBePlacedOnGlobalLayout;
import ru.droogcompanii.application.util.CurrentLocationProvider;

/**
 * Created by ls on 10.01.14.
 */
class BaseCustomMapFragment extends FragmentAbleRunOnceOnResume
        implements MarkersFinder, LocationSource.OnLocationChangedListener {

    private static final LocationSource.OnLocationChangedListener
            DUMMY_ON_LOCATION_CHANGED_LISTENER = new LocationSource.OnLocationChangedListener() {
        @Override
        public void onLocationChanged(Location location) {
            // do nothing
        }
    };

    private boolean isFirstUpdatingMapCamera;
    private boolean isMapViewPlacedOnLayout;
    private GoogleMap map;
    private List<Marker> markers;
    private LocationSource.OnLocationChangedListener onLocationChangedListener;


    public BaseCustomMapFragment() {
        isMapViewPlacedOnLayout = false;
        markers = new ArrayList<Marker>();
        map = null;
        onLocationChangedListener = DUMMY_ON_LOCATION_CHANGED_LISTENER;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onLocationChangedListener = DUMMY_ON_LOCATION_CHANGED_LISTENER;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirstUpdatingMapCamera = (savedInstanceState == null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_custom_map, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDisplayOfCurrentLocation();
    }

    private void initDisplayOfCurrentLocation() {
        GoogleMap googleMap = getGoogleMap();
        if (googleMap == null) {
            return;
        }

        googleMap.setMyLocationEnabled(true);
        CurrentLocationProvider.setOnLocationChangedListener(this);

        googleMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                BaseCustomMapFragment.this.onLocationChangedListener = onLocationChangedListener;
                CurrentLocationProvider.updateCurrentLocation();
            }

            @Override
            public void deactivate() {
                BaseCustomMapFragment.this.onLocationChangedListener = DUMMY_ON_LOCATION_CHANGED_LISTENER;
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            location = DroogCompaniiSettings.getDefaultBaseLocation();
        }
        onLocationChangedListener.onLocationChanged(location);
    }

    protected final GoogleMap getGoogleMap() {
        if (isNeedToInitMap()) {
            initMap();
        }
        return map;
    }

    private boolean isNeedToInitMap() {
        return (map == null) && (getActivity() != null) &&
                (getActivity().getSupportFragmentManager() != null);
    }

    private void initMap() {
        SupportMapFragment mapFragment = getNestedSupportMapFragment();
        if (mapFragment != null) {
            map = mapFragment.getMap();
        }
    }

    private SupportMapFragment getNestedSupportMapFragment() {
        return (SupportMapFragment)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.mapView);
    }

    private final View getMapView() {
        return getNestedSupportMapFragment().getView();
    }

    public final Marker addMarker(MarkerOptions markerOptions) {
        Marker marker = getGoogleMap().addMarker(markerOptions);
        markers.add(marker);
        return marker;
    }

    protected final void removeAllMarkers() {
        getGoogleMap().clear();
        markers.clear();
    }

    protected final void updateMapCameraAfterMapViewWillBePlacedOnLayout(
                                    final ClickedMarkerHolder clickedMarker) {
        if (isMapViewPlacedOnLayout) {
            updateMapCamera(clickedMarker);
        } else {
            ObserverOfViewWillBePlacedOnGlobalLayout.runAfterViewWillBePlacedOnLayout(getMapView(), new Runnable() {
                @Override
                public void run() {
                    updateMapCamera(clickedMarker);
                    isMapViewPlacedOnLayout = true;
                }
            });
        }
    }

    protected void updateMapCamera(ClickedMarkerHolder clickedMarker) {
        if (clickedMarker.isPresent()) {
            moveCamera(clickedMarker.getPosition());
        } else {
            tryMoveCameraToCurrentLocation();
        }
        isFirstUpdatingMapCamera = false;
    }

    private void moveCamera(LatLng center) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(center, defineZoom());
        getGoogleMap().animateCamera(cameraUpdate);
    }

    private float defineZoom() {
        if (isFirstUpdatingMapCamera) {
            return DroogCompaniiSettings.getDefaultZoom();
        } else {
            return getCurrentZoom();
        }
    }

    private float getCurrentZoom() {
        return getGoogleMap().getCameraPosition().zoom;
    }

    private void tryMoveCameraToCurrentLocation() {
        Location baseLocation = DroogCompaniiSettings.getBaseLocation();
        if (baseLocation != null) {
            moveCamera(positionOf(baseLocation));
        } else {
            fitVisibleMarkersOnScreen();
        }
    }

    private static LatLng positionOf(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    private void fitVisibleMarkersOnScreen() {
        MapCameraUpdater.fitVisibleMarkers(getGoogleMap(), markers, getMapPadding());
    }

    private int getMapPadding() {
        return getResources().getInteger(R.integer.map_markers_padding);
    }

    @Override
    public Marker findMarkerByPosition(LatLng position) {
        for (Marker marker : markers) {
            if (position.equals(marker.getPosition())) {
                return marker;
            }
        }
        return null;
    }

    @Override
    public boolean isMarkerPlacedOnMap(Marker marker) {
        return markers.contains(marker);
    }
}
