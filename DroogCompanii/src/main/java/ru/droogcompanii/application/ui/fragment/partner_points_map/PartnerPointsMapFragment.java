package ru.droogcompanii.application.ui.fragment.partner_points_map;

import android.app.Activity;
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
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.util.MultiMap;
import ru.droogcompanii.application.util.CurrentLocationProvider;

/**
 * Created by ls on 14.01.14.
 */
public class PartnerPointsMapFragment extends BaseCustomMapFragment
        implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {


    public static interface Callbacks {
        void onNeedToShowPartnerPoints(Set<PartnerPoint> partnerPointsToShow);
        void onNoLongerNeedToShowPartnerPoints();
    }

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
        Serializable searchablePartnerPoints = savedInstanceState.getSerializable(Keys.searchablePartnerPoints);
        setPartnerPointsIfNeed((SearchableListing<PartnerPoint>) searchablePartnerPoints);
    }

    private void initOnceOnResume() {
        getGoogleMap().setOnMapClickListener(this);
        getGoogleMap().setOnMarkerClickListener(this);
        initIfNeed();
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
        updateCurrentLocationOnMap();
    }

    private void updateCurrentLocationOnMap() {
        CurrentLocationProvider.updateCurrentLocation();
        updateMapCamera(clickedMarkerHolder);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.searchablePartnerPoints, searchablePartnerPoints);
        clickedMarkerHolder.saveInto(outState);
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
}
