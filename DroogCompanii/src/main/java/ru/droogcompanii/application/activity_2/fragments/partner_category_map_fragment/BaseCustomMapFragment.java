package ru.droogcompanii.application.activity_2.fragments.partner_category_map_fragment;

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
class BaseCustomMapFragment extends android.support.v4.app.Fragment {

    private GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_partner_category_map, container, false);
    }

    protected GoogleMap getGoogleMap() {
        android.support.v4.app.FragmentActivity activity = getActivity();
        if ((map == null) && (activity != null) && (activity.getSupportFragmentManager() != null)) {
            SupportMapFragment mapFragment = getSupportMapFragment();
            if (mapFragment != null) {
                map = mapFragment.getMap();
            }
        }
        return map;
    }

    protected SupportMapFragment getSupportMapFragment() {
        return (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mapView);
    }

}
