package ru.droogcompanii.application.view.fragment.partner_points_map_fragment;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableSortableListing;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.util.MultiMap;
import ru.droogcompanii.application.util.latlng_bounds_calculator.LatLngBoundsCalculator;
import ru.droogcompanii.application.view.fragment.BaseCustomMapFragment;
import ru.droogcompanii.application.view.fragment.filter_fragment.filters.Filters;
import ru.droogcompanii.application.view.helpers.ObserverOfViewWillBePlacedOnGlobalLayout;

/**
 * Created by ls on 14.01.14.
 */
public class PartnerPointsMapFragment extends BaseCustomMapFragment implements GoogleMap.OnMarkerClickListener {

    public static interface OnNeedToShowPartnerPointsListener {
        void onNeedToShowPartnerPoints(Set<PartnerPoint> partnerPointsToShow);
    }

    private boolean mapViewIsPlacedOnLayout;
    private MultiMap<Marker, PartnerPoint> markersAndPartnerPoints;
    private Collection<Marker> markers;
    private OnNeedToShowPartnerPointsListener onNeedToShowPartnerPointsListener;
    private SearchableListing<PartnerPoint> searchablePartnerPoints;

    public PartnerPointsMapFragment() {
        super();
        mapViewIsPlacedOnLayout = false;
        searchablePartnerPoints = SearchableListing.newInstance(new ArrayList<PartnerPoint>());
        markersAndPartnerPoints = new MultiMap<Marker, PartnerPoint>();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onNeedToShowPartnerPointsListener = (OnNeedToShowPartnerPointsListener) activity;
    }

    public void setPartnerPointsProvider(PartnerPointsProvider partnerPointsProvider) {
        List<PartnerPoint> partnerPoints = partnerPointsProvider.getPartnerPoints(getActivity());
        searchablePartnerPoints = SearchableSortableListing.newInstance(partnerPoints);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getGoogleMap().setOnMarkerClickListener(this);

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
        MarkersIncluder markersIncluder = new MarkersIncluder(searchablePartnerPoints);
        markersAndPartnerPoints = markersIncluder.includeIn(getGoogleMap());
        markers = markersAndPartnerPoints.keySet();
    }

    private void fitVisibleMarkersOnScreenAfterMapViewWillBePlacedOnLayout() {
        if (mapViewIsPlacedOnLayout) {
            fitVisibleMarkersOnScreen();
        } else {
            ObserverOfViewWillBePlacedOnGlobalLayout.runAfterViewWillBePlacedOnLayout(getMapView(), new Runnable() {
                @Override
                public void run() {
                    fitVisibleMarkersOnScreen();
                    mapViewIsPlacedOnLayout = true;
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
        searchablePartnerPoints.removeAllFilters();
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
    public boolean onMarkerClick(Marker marker) {
        Set<PartnerPoint> partnerPointsToShow = markersAndPartnerPoints.get(marker);
        onNeedToShowPartnerPointsListener.onNeedToShowPartnerPoints(partnerPointsToShow);
        return true;
    }
}
