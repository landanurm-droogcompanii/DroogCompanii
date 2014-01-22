package ru.droogcompanii.application.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 10.01.14.
 */
public class BaseCustomMapFragment extends android.support.v4.app.Fragment {

    private GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_partner_category_map, container, false);
    }

    protected GoogleMap getGoogleMap() {
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

    protected View getMapView() {
        return getNestedSupportMapFragment().getView();
    }

}
