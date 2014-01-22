package ru.droogcompanii.application.view.fragment.partner_points_map_fragment;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableSortableListing;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.util.latlng_bounds_calculator.LatLngBoundsCalculator;
import ru.droogcompanii.application.view.fragment.BaseCustomMapFragment;
import ru.droogcompanii.application.view.fragment.MarkerOptionsBuilder;
import ru.droogcompanii.application.view.fragment.filter_fragment.filters.Filters;
import ru.droogcompanii.application.view.helpers.ObserverOfViewWillBePlacedOnGlobalLayout;

/**
 * Created by ls on 14.01.14.
 */
public class PartnerPointsMapFragment extends BaseCustomMapFragment implements GoogleMap.OnInfoWindowClickListener {

    public static interface OnPartnerPointInfoWindowClickListener {
        void onPartnerPointInfoWindowClick(PartnerPoint partnerPoint);
    }

    private boolean mapViewPlacedOnLayout;
    private List<Marker> markers;
    private List<PartnerPoint> partnerPoints;
    private OnPartnerPointInfoWindowClickListener onPartnerPointInfoWindowClickListener;
    private SearchableListing<PartnerPoint> searchablePartnerPoints;

    public PartnerPointsMapFragment() {
        super();
        mapViewPlacedOnLayout = false;
        searchablePartnerPoints = SearchableListing.newInstance(new ArrayList<PartnerPoint>());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onPartnerPointInfoWindowClickListener = (OnPartnerPointInfoWindowClickListener) activity;
    }

    public void setPartnerPointsProvider(PartnerPointsProvider partnerPointsProvider) {
        List<PartnerPoint> partnerPoints = partnerPointsProvider.getPartnerPoints(getActivity());
        searchablePartnerPoints = SearchableSortableListing.newInstance(partnerPoints);
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
        searchablePartnerPoints = (SearchableSortableListing<PartnerPoint>)
                savedInstanceState.getSerializable(Keys.searchableSortablePartnerPoints);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.searchableSortablePartnerPoints, searchablePartnerPoints);
    }

    private void updateMap() {
        updateMarkers();
        fitVisibleMarkersOnScreenAfterMapViewWillBePlacedOnLayout();
    }

    private void updateMarkers() {
        getGoogleMap().clear();
        placeMarkersOnMap();
    }

    private void placeMarkersOnMap() {
        GoogleMap googleMap = getGoogleMap();
        MarkerOptionsBuilder markerOptionsBuilder = new MarkerOptionsBuilder();
        markers = new ArrayList<Marker>();
        partnerPoints = searchablePartnerPoints.toList();
        for (PartnerPoint each : partnerPoints) {
            MarkerOptions markerOptions = markerOptionsBuilder.buildFrom(each);
            Marker marker = googleMap.addMarker(markerOptions);
            markers.add(marker);
        }
    }

    private void fitVisibleMarkersOnScreenAfterMapViewWillBePlacedOnLayout() {
        if (mapViewPlacedOnLayout) {
            fitVisibleMarkersOnScreen();
        } else {
            ObserverOfViewWillBePlacedOnGlobalLayout.runAfterViewWillBePlacedOnLayout(getMapView(), new Runnable() {
                @Override
                public void run() {
                    fitVisibleMarkersOnScreen();
                    mapViewPlacedOnLayout = true;
                }
            });
        }
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

    public void setFilters(Filters filters) {
        searchablePartnerPoints.clearAllCriteria();
        addFilters(filters);
    }

    public void addFilters(Filters filters) {
        List<SearchableListing.SearchCriterion<PartnerPoint>> searchCriteria = filters.searchCriteria;
        for (SearchableListing.SearchCriterion<PartnerPoint> criterion : searchCriteria) {
            searchablePartnerPoints.addSearchCriterion(criterion);
        }
        updateMap();
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
