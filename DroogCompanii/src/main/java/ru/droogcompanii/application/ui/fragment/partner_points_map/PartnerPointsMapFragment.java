package ru.droogcompanii.application.ui.fragment.partner_points_map;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableSortableListing;
import ru.droogcompanii.application.ui.fragment.filter.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter.FilterUtils;
import ru.droogcompanii.application.ui.util.CustomBaseLocationUtils;
import ru.droogcompanii.application.ui.util.LocationUtils;
import ru.droogcompanii.application.util.MultiMap;

/**
 * Created by ls on 14.01.14.
 */
public class PartnerPointsMapFragment extends BasePartnerPointsMapFragment
        implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    public static interface Callbacks {
        void onNeedToShowPartnerPoints(Set<PartnerPoint> partnerPointsToShow);
        void onNoLongerNeedToShowPartnerPoints();
        void onCustomBaseLocationIsSet();
    }

    private static final String KEY_SEARCHABLE_PARTNER_POINTS = "KEY_SEARCHABLE_PARTNER_POINTS";

    private boolean doNotInitOnResume;
    private boolean wasActivityCreated;
    private Callbacks callbacks;
    private ClickedMarkerHolder clickedMarkerHolder;
    private MultiMap<Marker, PartnerPoint> markersAndPartnerPoints;
    private SearchableListing<PartnerPoint> searchablePartnerPoints;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        wasActivityCreated = true;

        if (savedInstanceState == null) {
            initByDefault();
        } else {
            restoreState(savedInstanceState);
        }

        super.updateMapCameraAfterMapViewWillBePlacedOnLayout(clickedMarkerHolder);

        super.callOnceOnResume(new Runnable() {
            @Override
            public void run() {
                initOnceOnResume();
            }
        });
    }

    private void initByDefault() {
        clickedMarkerHolder = new ClickedMarkerHolder(this);
        setPartnerPointsIfNeed(SearchableListing.newInstance(new ArrayList<PartnerPoint>()));
    }

    private void setPartnerPointsIfNeed(SearchableListing<PartnerPoint> searchableListing) {
        if (this.searchablePartnerPoints == null) {
            this.searchablePartnerPoints = searchableListing;
        }
    }

    private void restoreState(Bundle savedInstanceState) {
        clickedMarkerHolder = new ClickedMarkerHolder(this);
        clickedMarkerHolder.restoreFrom(savedInstanceState);
        Serializable searchablePartnerPoints = savedInstanceState.getSerializable(KEY_SEARCHABLE_PARTNER_POINTS);
        setPartnerPointsIfNeed((SearchableListing<PartnerPoint>) searchablePartnerPoints);
    }

    private void initOnceOnResume() {
        initMapListeners(getGoogleMap());
        initIfNeed();
    }

    private void initMapListeners(GoogleMap googleMap) {
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);
    }

    private void initIfNeed() {
        if (!doNotInitOnResume) {
            init();
        }
    }

    private void init() {
        updateMap();
        clickedMarkerHolder.update();
    }

    private void updateMap() {
        super.removeAllMarkers();
        placeMarkersOnMap();
    }

    private void placeMarkersOnMap() {
        PartnerPointsGroupedByPosition grouped = new PartnerPointsGroupedByPosition(searchablePartnerPoints);
        markersAndPartnerPoints = new MultiMap<Marker, PartnerPoint>();
        for (LatLng position : grouped.getAllPositions()) {
            MarkerOptions options = prepareMarkerOptions(position);
            Marker marker = addMarker(options);
            Set<PartnerPoint> partnerPoints = grouped.get(position);
            markersAndPartnerPoints.putAll(marker, partnerPoints);
        }
    }

    private static MarkerOptions prepareMarkerOptions(LatLng position) {
        return new MarkerOptions()
                .position(position)
                .icon(MarkerIcons.unselected());
    }

    public void setPartnerPoints(Collection<PartnerPoint> partnerPoints) {
        searchablePartnerPoints = SearchableSortableListing.newInstance(partnerPoints);
        if (wasActivityCreated) {
            init();
        }
    }

    public void setDoNotInitOnResume() {
        doNotInitOnResume = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocationUtils.updateCurrentLocation();
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        updateFilterSet();
        updateMapCamera(clickedMarkerHolder);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_SEARCHABLE_PARTNER_POINTS, searchablePartnerPoints);
        clickedMarkerHolder.saveInto(outState);
    }

    public void updateFilterSet() {
        FilterSet currentFilterSet = FilterUtils.getCurrentFilterSet(getActivity());
        setFilterSet(currentFilterSet);
    }

    public void setFilterSet(FilterSet filterSet) {
        searchablePartnerPoints.removeAllFilters();
        searchablePartnerPoints.addSearchCriterion(filterSet.getCombinedPartnerPointSearchCriterion());
        updateMap();
        updateAfterFilteringIfNeed();
    }

    private void updateAfterFilteringIfNeed() {
        if (isNeedToUpdateAfterFiltering()) {
            updateAfterFiltering();
        }
    }

    private boolean isNeedToUpdateAfterFiltering() {
        return clickedMarkerHolder.isPresent();
    }

    private void updateAfterFiltering() {
        clickedMarkerHolder.update();
        if (clickedMarkerHolder.isAbsent()) {
            callbacks.onNoLongerNeedToShowPartnerPoints();
        } else {
            notifyNeedToShowPartnerPoints(clickedMarkerHolder.getMarker());
        }
    }

    private void notifyNeedToShowPartnerPoints(Marker marker) {
        Set<PartnerPoint> partnerPointsToShow = markersAndPartnerPoints.get(marker);
        callbacks.onNeedToShowPartnerPoints(partnerPointsToShow);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (isNotClickedMarker(marker)) {
            notifyNeedToShowPartnerPoints(marker);
            clickedMarkerHolder.set(marker);
        }
        updateMapCamera(clickedMarkerHolder);
        return true;
    }

    private boolean isNotClickedMarker(Marker marker) {
        return !clickedMarkerHolder.isHolding(marker);
    }

    @Override
    public void onMapClick(LatLng position) {
        setNoClickedMarker();
    }

    public void setNoClickedMarker() {
        callbacks.onNoLongerNeedToShowPartnerPoints();
        clickedMarkerHolder.unset();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        CustomBaseLocationUtils.updateBasePosition(latLng);
        LocationUtils.notifyListeners();
        callbacks.onCustomBaseLocationIsSet();
        NotifierAboutBaseMapLocationChanges.notify(getActivity());
    }

}