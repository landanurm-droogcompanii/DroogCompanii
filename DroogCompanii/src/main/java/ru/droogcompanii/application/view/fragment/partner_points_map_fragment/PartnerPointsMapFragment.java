package ru.droogcompanii.application.view.fragment.partner_points_map_fragment;

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
import ru.droogcompanii.application.data.SearchableSortable;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.view.helpers.ObserverOfViewWillBePlacedOnGlobalLayout;
import ru.droogcompanii.application.view.activity.partner_info_activity.latlng_bounds_calculator.LatLngBoundsCalculator;
import ru.droogcompanii.application.view.fragment.BaseCustomMapFragment;
import ru.droogcompanii.application.view.fragment.MarkerOptionsBuilder;

/**
 * Created by ls on 14.01.14.
 */
public class PartnerPointsMapFragment extends BaseCustomMapFragment implements GoogleMap.OnInfoWindowClickListener {


    public static interface OnPartnerPointInfoWindowClickListener {
        void onPartnerPointInfoWindowClick(PartnerPoint partnerPoint);
    }

    private List<PartnerPoint> partnerPoints;
    private List<Marker> markers;
    private OnPartnerPointInfoWindowClickListener onPartnerPointInfoWindowClickListener;
    private PartnerPointsProvider partnerPointsProvider;

    public PartnerPointsMapFragment() {
        super();
        partnerPoints = new ArrayList<PartnerPoint>();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onPartnerPointInfoWindowClickListener = (OnPartnerPointInfoWindowClickListener) activity;
    }

    public void setPartnerPointsProvider(PartnerPointsProvider partnerPointsProvider) {
        this.partnerPointsProvider = partnerPointsProvider;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getGoogleMap().setOnInfoWindowClickListener(this);

        if (savedInstanceState == null) {
            partnerPoints = partnerPointsProvider.getPartnerPoints(getActivity());
        } else {
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

    private void updateMap() {
        updateMarkers();
        fitVisibleMarkersOnScreenAfterMapViewWillBePlacedOnLayout();
    }

    private void updateMarkers() {
        final GoogleMap googleMap = getGoogleMap();
        googleMap.clear();
        markers = new ArrayList<Marker>();
        SearchableSortable<PartnerPoint> searchableSortablePartnerPoints = SearchableSortable.newInstance(partnerPoints);
        final MarkerOptionsBuilder markerOptionsBuilder = new MarkerOptionsBuilder();
        searchableSortablePartnerPoints.forEach(new SearchableSortable.OnEachHandler<PartnerPoint>() {
            @Override
            public void onEach(PartnerPoint each, boolean meetsCriteria) {
                if (!meetsCriteria) {
                    return;
                }
                MarkerOptions markerOptions = markerOptionsBuilder.buildFrom(each);
                Marker marker = googleMap.addMarker(markerOptions);
                markers.add(marker);
            }
        });
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
