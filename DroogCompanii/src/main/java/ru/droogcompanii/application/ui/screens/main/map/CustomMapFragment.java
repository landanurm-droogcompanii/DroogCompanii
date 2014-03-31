package ru.droogcompanii.application.ui.screens.main.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.ui.able_to_start_task.FragmentAbleToStartTask;

/**
 * Created by ls on 14.03.14.
 */
public class CustomMapFragment extends FragmentAbleToStartTask {

    private GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_custom_map, container, false);
    }

    public GoogleMap getGoogleMap() {
        if (isNeedToInitMap()) {
            initMap();
        }
        return map;
    }

    private boolean isNeedToInitMap() {
        return (map == null) && (getActivity() != null) &&
                (getActivity().getSupportFragmentManager() != null);
    }

    public LatLngBounds getBounds() {
        return getGoogleMap().getProjection().getVisibleRegion().latLngBounds;
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

    public View getMapView() {
        return getNestedSupportMapFragment().getView();
    }

    public float getMaxZoom() {
        return getGoogleMap().getMaxZoomLevel();
    }

    public float getCurrentZoom() {
        return getGoogleMap().getCameraPosition().zoom;
    }

    public void moveCamera(LatLng center, float zoom) {
        getGoogleMap().moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoom));
    }

    public void animateCamera(LatLng center, float zoom) {
        getGoogleMap().animateCamera(CameraUpdateFactory.newLatLngZoom(center, zoom));
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
        // do nothing
    }

}
