package ru.droogcompanii.application.ui.fragment.partner_points_map_fragment;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.helpers.ObserverOfViewWillBePlacedOnGlobalLayout;
import ru.droogcompanii.application.util.CurrentLocationProvider;
import ru.droogcompanii.application.util.latlng_bounds_calculator.LatLngBoundsCalculator;

/**
 * Created by ls on 10.01.14.
 */
class BaseCustomMapFragment extends android.support.v4.app.Fragment implements MarkersFinder {
    private static final int ZOOM = 9;

    private boolean isMapViewPlacedOnLayout;
    private GoogleMap map;
    private List<Marker> markers;

    public BaseCustomMapFragment() {
        isMapViewPlacedOnLayout = false;
        markers = new ArrayList<Marker>();
        map = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_custom_map, container, false);
    }

    protected final GoogleMap getGoogleMap() {
        if (needToInitMap()) {
            initMap();
        }
        return map;
    }

    private boolean needToInitMap() {
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
        return (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mapView);
    }

    protected final View getMapView() {
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

    protected final void updateMapCameraAfterMapViewWillBePlacedOnLayout(final ClickedMarkerHolder clickedMarker) {
        if (isMapViewPlacedOnLayout) {
            updateMapCamera();
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

    private void updateMapCamera() {
        tryMoveCameraToCurrentLocation();
    }

    protected void updateMapCamera(ClickedMarkerHolder clickedMarker) {
        if (clickedMarker.isPresent()) {
            moveCamera(clickedMarker.getPosition(), ZOOM);
        } else {
            tryMoveCameraToCurrentLocation();
        }
    }

    protected void moveCamera(LatLng center, int zoom) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(center, zoom);
        getGoogleMap().animateCamera(cameraUpdate);
    }

    private void tryMoveCameraToCurrentLocation() {
        Location currentLocation = CurrentLocationProvider.get();
        if (currentLocation != null) {
            moveCamera(positionOf(currentLocation), ZOOM);
        } else {
            fitVisibleMarkersOnScreen();
        }
    }

    private static LatLng positionOf(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    private void fitVisibleMarkersOnScreen() {
        if (noVisibleMarkers()) {
            return;
        }
        LatLngBounds bounds = LatLngBoundsCalculator.calculateBoundsOfVisibleMarkers(markers);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, getMapPadding());
        getGoogleMap().moveCamera(cameraUpdate);
    }

    private boolean noVisibleMarkers() {
        for (Marker each : markers) {
            if (each.isVisible()) {
                return false;
            }
        }
        return true;
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
        return markers.indexOf(marker) != -1;
    }
}
