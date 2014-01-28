package ru.droogcompanii.application.view.fragment.partner_points_map_fragment;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableSortableListing;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.util.MultiMap;
import ru.droogcompanii.application.view.fragment.filter_fragment.FilterSet;

/**
 * Created by ls on 14.01.14.
 */
public class PartnerPointsMapFragment extends BaseCustomMapFragment
        implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    public static interface OnNeedToShowPartnerPointsListener {
        void onNeedToShowPartnerPoints(Set<PartnerPoint> partnerPointsToShow);
        void onNoLongerNeedToShowPartnerPoints();
    }

    private OnNeedToShowPartnerPointsListener onNeedToShowPartnerPointsListener;

    private ClickedMarkerHolder clickedMarkerHolder;
    private MultiMap<Marker, PartnerPoint> markersAndPartnerPoints;
    private SearchableListing<PartnerPoint> searchablePartnerPoints;


    public PartnerPointsMapFragment() {
        super();
        searchablePartnerPoints = SearchableListing.newInstance(new ArrayList<PartnerPoint>());
        clickedMarkerHolder = new ClickedMarkerHolder(this);
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

        getGoogleMap().setOnMapClickListener(this);
        getGoogleMap().setOnMarkerClickListener(this);

        restoreInstanceStateIfNeed(savedInstanceState);
        updateMap();

        clickedMarkerHolder.restoreFromIfNeed(savedInstanceState);
    }

    @SuppressWarnings("unchecked")
    private void restoreInstanceStateIfNeed(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        searchablePartnerPoints = (SearchableSortableListing<PartnerPoint>)
                savedInstanceState.getSerializable(Keys.searchableSortablePartnerPoints);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.searchableSortablePartnerPoints, searchablePartnerPoints);
        clickedMarkerHolder.saveInto(outState);
    }

    private void updateMap() {
        updateMarkers();
        fitVisibleMarkersOnScreenAfterMapViewWillBePlacedOnLayout();
    }

    private void updateMarkers() {
        super.removeAllMarkers();
        placeMarkersOnMap();
    }

    private void placeMarkersOnMap() {
        PartnerPointsGroupedByPosition grouped = new PartnerPointsGroupedByPosition(searchablePartnerPoints);
        markersAndPartnerPoints = new MultiMap<Marker, PartnerPoint>();
        for (LatLng position : grouped.getAllPositions()) {
            Marker marker = addMarker(prepareMarkerOptions(position));
            Set<PartnerPoint> partnerPoints = grouped.get(position);
            markersAndPartnerPoints.putAll(marker, partnerPoints);
        }
    }

    private static MarkerOptions prepareMarkerOptions(LatLng position) {
        return new MarkerOptions().position(position).icon(MarkerIcons.unselected());
    }

    public void setFilterSet(FilterSet filterSet) {
        searchablePartnerPoints.removeAllFilters();
        addFilterSet(filterSet);
    }

    public void addFilterSet(FilterSet filterSet) {
        List<? extends SearchCriterion<PartnerPoint>> searchCriteria = filterSet.getSearchCriteria();
        for (SearchCriterion<PartnerPoint> criterion : searchCriteria) {
            searchablePartnerPoints.addSearchCriterion(criterion);
        }
        updateMap();
        updateAfterFilteringIfNeed();
    }

    private void updateAfterFilteringIfNeed() {
        if (clickedMarkerHolder.isAbsent()) {
            return;
        }
        updateAfterFiltering();
    }

    private void updateAfterFiltering() {
        clickedMarkerHolder.update();
        if (clickedMarkerHolder.isAbsent()) {
            onNeedToShowPartnerPointsListener.onNoLongerNeedToShowPartnerPoints();
        } else {
            notifyNeedToShowPartnerPoints(clickedMarkerHolder.getMarker());
        }
    }

    private void notifyNeedToShowPartnerPoints(Marker marker) {
        Set<PartnerPoint> partnerPointsToShow = markersAndPartnerPoints.get(marker);
        onNeedToShowPartnerPointsListener.onNeedToShowPartnerPoints(partnerPointsToShow);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        notifyNeedToShowPartnerPoints(marker);
        clickedMarkerHolder.set(marker);
        return true;
    }

    @Override
    public void onMapClick(LatLng position) {
        onNeedToShowPartnerPointsListener.onNoLongerNeedToShowPartnerPoints();
        clickedMarkerHolder.unset();
    }
}
