package ru.droogcompanii.application.ui.fragment.partner_points_map_fragment;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableSortableListing;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterSet;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.util.MultiMap;

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
    private Bundle savedInstanceState;
    private Callbacks callbacks;
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
        callbacks = (Callbacks) activity;
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.savedInstanceState = savedInstanceState;

        wasActivityCreated = true;

        clickedMarkerHolder.restoreFromIfNeed(savedInstanceState);

        getGoogleMap().setOnMapClickListener(this);
        getGoogleMap().setOnMarkerClickListener(this);

        restoreInstanceStateIfNeed(savedInstanceState);

        super.callOnResumeFirstTime(new Runnable() {
            @Override
            public void run() {
                initIfNeed();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void restoreInstanceStateIfNeed(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            searchablePartnerPoints = (SearchableListing<PartnerPoint>)
                    savedInstanceState.getSerializable(Keys.searchablePartnerPoints);
        }
    }

    private void initIfNeed() {
        if (!doNotInitOnResume) {
            init();
        }
    }

    private void init() {
        updateMap();
        clickedMarkerHolder.update();
        super.updateMapCameraAfterMapViewWillBePlacedOnLayout(clickedMarkerHolder);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.searchablePartnerPoints, searchablePartnerPoints);
        clickedMarkerHolder.saveInto(outState);
    }

    private void updateMap() {
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
        addSearchFilters(filterSet);
        updateMap();
        updateAfterFilteringIfNeed();
    }

    private void addSearchFilters(FilterSet filterSet) {
        List<? extends SearchCriterion<PartnerPoint>> searchCriteria =
                filterSet.getPartnerPointSearchCriteria();
        for (SearchCriterion<PartnerPoint> criterion : searchCriteria) {
            searchablePartnerPoints.addSearchCriterion(criterion);
        }
    }

    private void updateAfterFilteringIfNeed() {
        if (needToUpdateAfterFiltering()) {
            updateAfterFiltering();
        }
    }

    private boolean needToUpdateAfterFiltering() {
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
        if (isMarkerAlreadyClicked(marker)) {
            return true;
        }
        notifyNeedToShowPartnerPoints(marker);
        clickedMarkerHolder.set(marker);
        updateMapCamera(clickedMarkerHolder);
        return true;
    }

    private boolean isMarkerAlreadyClicked(Marker marker) {
        return clickedMarkerHolder.isHolding(marker);
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
