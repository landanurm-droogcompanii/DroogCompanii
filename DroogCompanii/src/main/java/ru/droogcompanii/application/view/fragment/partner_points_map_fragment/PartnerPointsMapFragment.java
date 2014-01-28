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

    private Marker clickedMarker;
    private MultiMap<Marker, PartnerPoint> markersAndPartnerPoints;
    private SearchableListing<PartnerPoint> searchablePartnerPoints;


    public PartnerPointsMapFragment() {
        super();
        searchablePartnerPoints = SearchableListing.newInstance(new ArrayList<PartnerPoint>());
        clickedMarker = null;
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
        restoreClickedMarkerIfNeed(savedInstanceState);
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
        savePositionOfClickedMarker(outState);
    }

    private void savePositionOfClickedMarker(Bundle bundle) {
        if (noClickedMarker()) {
            bundle.putBoolean(Keys.clickedMarkerIsExist, false);
        } else {
            bundle.putBoolean(Keys.clickedMarkerIsExist, true);
            savePosition(bundle, clickedMarker.getPosition());
        }
    }

    private static void savePosition(Bundle bundle, LatLng position) {
        bundle.putDouble(Keys.latitude, position.latitude);
        bundle.putDouble(Keys.longitude, position.longitude);
    }

    private void restoreClickedMarkerIfNeed(Bundle bundle) {
        if (needToRestoreClickedMarker(bundle)) {
            restoreClickedMarker(bundle);
        }
    }

    private static boolean needToRestoreClickedMarker(Bundle bundle) {
        return (bundle != null) && bundle.getBoolean(Keys.clickedMarkerIsExist);
    }

    private void restoreClickedMarker(Bundle bundle) {
        LatLng positionOfClickedMarker = readPosition(bundle);
        Marker marker = findMarkerByPosition(positionOfClickedMarker);
        setClickedMarker(marker);
    }

    private static LatLng readPosition(Bundle bundle) {
        double latitude = bundle.getDouble(Keys.latitude);
        double longitude = bundle.getDouble(Keys.longitude);
        return new LatLng(latitude, longitude);
    }

    private boolean noClickedMarker() {
        return clickedMarker == null;
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
        updateAfterFiltering();
    }

    private void updateAfterFiltering() {
        if (noClickedMarker()) {
            return;
        }
        Marker actualClickedMarker = getActualClickedMarker(clickedMarker);
        if (actualClickedMarker == null) {
            onNeedToShowPartnerPointsListener.onNoLongerNeedToShowPartnerPoints();
            return;
        }
        setClickedMarker(actualClickedMarker);
        notifyNeedToShowPartnerPoints(actualClickedMarker);
    }

    private Marker getActualClickedMarker(Marker oldClickedMarker) {
        LatLng positionOfClickedMarker = oldClickedMarker.getPosition();
        return super.findMarkerByPosition(positionOfClickedMarker);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        notifyNeedToShowPartnerPoints(marker);
        setClickedMarker(marker);
        return true;
    }

    private void notifyNeedToShowPartnerPoints(Marker marker) {
        Set<PartnerPoint> partnerPointsToShow = markersAndPartnerPoints.get(marker);
        onNeedToShowPartnerPointsListener.onNeedToShowPartnerPoints(partnerPointsToShow);
    }

    void setClickedMarker(Marker marker) {
        unselectClickedMarker();
        selectMarker(marker);
        clickedMarker = marker;
    }

    private void unselectClickedMarker() {
        if (noClickedMarker()) {
            return;
        }
        if (isMarkerPlacedOnMap(clickedMarker)) {
            unselectMarker(clickedMarker);
        }
        clickedMarker = null;
    }

    private void unselectMarker(Marker marker) {
        marker.setIcon(MarkerIcons.unselected());
    }

    private void selectMarker(Marker marker) {
        marker.setIcon(MarkerIcons.selected());
    }

    @Override
    public void onMapClick(LatLng position) {
        onNeedToShowPartnerPointsListener.onNoLongerNeedToShowPartnerPoints();
        unselectClickedMarker();
    }
}
