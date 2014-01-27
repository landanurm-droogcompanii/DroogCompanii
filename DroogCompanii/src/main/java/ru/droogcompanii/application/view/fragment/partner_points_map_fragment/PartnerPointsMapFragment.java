package ru.droogcompanii.application.view.fragment.partner_points_map_fragment;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableSortableListing;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.view.fragment.BaseCustomMapFragment;
import ru.droogcompanii.application.view.fragment.filter_fragment.FilterSet;

/**
 * Created by ls on 14.01.14.
 */
public class PartnerPointsMapFragment extends BaseCustomMapFragment
        implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private static final GoogleMap.OnMapClickListener DUMMY_ON_MAP_CLICK_LISTENER =
            new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    // do nothing
                }
            };

    public static interface OnNeedToShowPartnerPointsListener {
        void onNeedToShowPartnerPoints(Set<PartnerPoint> partnerPointsToShow);
        void onNoLongerNeedToShowPartnerPoints();
    }

    private GoogleMap.OnMapClickListener onMapClickListener;
    private OnNeedToShowPartnerPointsListener onNeedToShowPartnerPointsListener;

    private Marker clickedMarker;
    private PositionsAndPartnerPoints positionsAndPartnerPoints;
    private SearchableListing<PartnerPoint> searchablePartnerPoints;

    public PartnerPointsMapFragment() {
        super();
        searchablePartnerPoints = SearchableListing.newInstance(new ArrayList<PartnerPoint>());
        onMapClickListener = DUMMY_ON_MAP_CLICK_LISTENER;
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

        restoreInstanceState(savedInstanceState);
        updateMap();
        restoreClickedMarkerIfNeed(savedInstanceState);
    }

    private void restoreClickedMarkerIfNeed(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        boolean clickedMarkerExist = bundle.getBoolean(Keys.clickedMarkerExist);
        if (clickedMarkerExist) {
            restoreClickedMarker(bundle);
        }
    }

    private void restoreClickedMarker(Bundle bundle) {
        LatLng positionOfClickedMarker = readPositionOfClickedMarker(bundle);
        Marker marker = findMarkerByPosition(positionOfClickedMarker);
        setClickedMarker(marker);
    }

    private static LatLng readPositionOfClickedMarker(Bundle bundle) {
        double latitude = bundle.getDouble(Keys.latitudeOfClickedMarker);
        double longitude = bundle.getDouble(Keys.longitudeOfClickedMarker);
        return new LatLng(latitude, longitude);
    }

    @SuppressWarnings("unchecked")
    private void restoreInstanceState(Bundle savedInstanceState) {
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
            bundle.putBoolean(Keys.clickedMarkerExist, false);
        } else {
            bundle.putBoolean(Keys.clickedMarkerExist, true);
            LatLng positionOfClickedMarker = clickedMarker.getPosition();
            bundle.putDouble(Keys.latitudeOfClickedMarker, positionOfClickedMarker.latitude);
            bundle.putDouble(Keys.longitudeOfClickedMarker, positionOfClickedMarker.longitude);
        }
    }

    private boolean noClickedMarker() {
        return clickedMarker == null;
    }

    private void updateMap() {
        updateMarkers();
        fitVisibleMarkersOnScreenAfterMapViewWillBePlacedOnLayout();
    }

    private void updateMarkers() {
        removeAllMarkers();
        placeMarkersOnMap();
    }

    private void placeMarkersOnMap() {
        MarkersIncluder markersIncluder = new MarkersIncluder(searchablePartnerPoints);
        positionsAndPartnerPoints = markersIncluder.includeIn(this);
    }

    private void updateAfterFiltering() {
        if (noClickedMarker()) {
            return;
        }
        LatLng positionOfClickedMarker = clickedMarker.getPosition();
        Marker marker = findMarkerByPosition(positionOfClickedMarker);
        if (marker != null) {
            Set<PartnerPoint> afterFiltering = positionsAndPartnerPoints.get(positionOfClickedMarker);
            onNeedToShowPartnerPointsListener.onNeedToShowPartnerPoints(afterFiltering);
            setClickedMarker(marker);
        } else {
            unselectClickedMarker();
            onNeedToShowPartnerPointsListener.onNoLongerNeedToShowPartnerPoints();
        }
    }

    public void setFilterSet(FilterSet filterSet) {
        searchablePartnerPoints.removeAllFilters();
        addFilters(filterSet);
    }

    public void addFilters(FilterSet filterSet) {
        List<? extends SearchableListing.SearchCriterion<PartnerPoint>> searchCriteria =
                filterSet.getSearchCriteria();
        for (SearchableListing.SearchCriterion<PartnerPoint> criterion : searchCriteria) {
            searchablePartnerPoints.addSearchCriterion(criterion);
        }
        updateMap();
        updateAfterFiltering();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        notifyNeedToShowPartnerPoints(marker.getPosition());
        unselectClickedMarker();
        setClickedMarker(marker);
        return true;
    }

    private void notifyNeedToShowPartnerPoints(LatLng position) {
        Set<PartnerPoint> partnerPointsToShow = positionsAndPartnerPoints.get(position);
        onNeedToShowPartnerPointsListener.onNeedToShowPartnerPoints(partnerPointsToShow);
    }

    void setClickedMarker(Marker marker) {
        MarkerUtils.select(marker);
        clickedMarker = marker;
    }

    private void unselectClickedMarker() {
        if (noClickedMarker()) {
            return;
        }
        MarkerUtils.unselect(clickedMarker);
        clickedMarker = null;
    }

    @Override
    public void onMapClick(LatLng position) {
        onMapClickListener.onMapClick(position);
        unselectClickedMarker();
    }

    public void setOnMapClickListener(GoogleMap.OnMapClickListener listener) {
        onMapClickListener = listener;
    }
}
