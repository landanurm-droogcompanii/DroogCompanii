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
public class PartnerCategoryMapFragment extends android.support.v4.app.Fragment {

    private GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_partner_category_map, container, false);
    }

    private GoogleMap getGoogleMap() {
        android.support.v4.app.FragmentActivity activity = getActivity();
        if ((map == null) && (activity != null) && (activity.getSupportFragmentManager() != null)) {
            android.support.v4.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
            SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.mapView);
            if (mapFragment != null) {
                map = mapFragment.getMap();
            }
        }
        return map;
    }


}
