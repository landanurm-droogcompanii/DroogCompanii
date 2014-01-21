package ru.droogcompanii.application.view.fragment.partner_category_map_fragment;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerPointsReader;
import ru.droogcompanii.application.view.helpers.ObserverOfViewWillBePlacedOnGlobalLayout;
import ru.droogcompanii.application.util.latlng_bounds_calculator.LatLngBoundsCalculator;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.view.fragment.BaseCustomMapFragment;
import ru.droogcompanii.application.view.fragment.MarkerOptionsBuilder;

/**
 * Created by ls on 10.01.14.
 */
public class PartnerCategoryMapFragment extends BaseCustomMapFragment
        implements GoogleMap.OnInfoWindowClickListener {

    public static interface OnPartnerPointInfoWindowClickListener {
        void onPartnerPointInfoWindowClick(PartnerPoint partnerPoint);
    }

    private List<PartnerPoint> partnerPoints;
    private List<Marker> markers;
    private OnPartnerPointInfoWindowClickListener onPartnerPointInfoWindowClickListener;


    public PartnerCategoryMapFragment() {
        super();
        partnerPoints = new ArrayList<PartnerPoint>();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onPartnerPointInfoWindowClickListener = (OnPartnerPointInfoWindowClickListener) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getGoogleMap().setOnInfoWindowClickListener(this);

        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }
        updateMap();
    }

    @SuppressWarnings("unchecked")
    private void restoreInstanceState(Bundle savedInstanceState) {
        partnerPoints = (List<PartnerPoint>) savedInstanceState.getSerializable(Keys.partnerPoints);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partnerPoints, (Serializable) partnerPoints);
    }

    public void update(PartnerCategory partnerCategory) {
        setPartnerCategory(partnerCategory);
        updateMap();
    }

    public void setPartnerCategory(PartnerCategory partnerCategory) {
        PartnerPointsReader partnerPointsReader = new PartnerPointsReader(getActivity());
        partnerPoints = partnerPointsReader.getPartnerPointsOf(partnerCategory);
    }

    private void updateMap() {
        getGoogleMap().clear();
        updateMarkers();
        fitVisibleMarkersOnScreenAfterMapViewWillBePlacedOnLayout();
    }

    private void updateMarkers() {
        markers = new ArrayList<Marker>();
        MarkerOptionsBuilder markerOptionsBuilder = new MarkerOptionsBuilder();
        GoogleMap googleMap = getGoogleMap();
        for (PartnerPoint partnerPoint : partnerPoints) {
            MarkerOptions markerOptions = markerOptionsBuilder.buildFrom(partnerPoint);
            Marker marker = googleMap.addMarker(markerOptions);
            markers.add(marker);
        }
    }

    private void fitVisibleMarkersOnScreenAfterMapViewWillBePlacedOnLayout() {
        ObserverOfViewWillBePlacedOnGlobalLayout.runAfterViewWillBePlacedOnLayout(getMapView(), new Runnable() {
            @Override
            public void run() {
                fitVisibleMarkersOnScreen();
            }
        });
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
    public void onInfoWindowClick(Marker marker) {
        PartnerPoint partnerPoint = partnerPointOf(marker);
        onPartnerPointInfoWindowClickListener.onPartnerPointInfoWindowClick(partnerPoint);
    }

    private PartnerPoint partnerPointOf(Marker marker) {
        int index = markers.indexOf(marker);
        return partnerPoints.get(index);
    }
}
