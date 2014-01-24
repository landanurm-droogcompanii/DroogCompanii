package ru.droogcompanii.application.view.fragment.partner_points_map_fragment;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
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
import ru.droogcompanii.application.view.fragment.filter_fragment.FilterSet;
import ru.droogcompanii.application.view.helpers.ObserverOfViewWillBePlacedOnGlobalLayout;

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

    private boolean mapViewIsPlacedOnLayout;
    private Collection<Marker> markers;
    private Marker clickedMarker;
    private MultiMap<Marker, PartnerPoint> markersAndPartnerPoints;
    private GoogleMap.OnMapClickListener onMapClickListener;
    private OnNeedToShowPartnerPointsListener onNeedToShowPartnerPointsListener;
    private SearchableListing<PartnerPoint> searchablePartnerPoints;

    public PartnerPointsMapFragment() {
        super();
        mapViewIsPlacedOnLayout = false;
        searchablePartnerPoints = SearchableListing.newInstance(new ArrayList<PartnerPoint>());
        markersAndPartnerPoints = new MultiMap<Marker, PartnerPoint>();
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
        restoreClickedMarker(savedInstanceState);
    }

    @SuppressWarnings("unchecked")
    private void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        searchablePartnerPoints = (SearchableSortableListing<PartnerPoint>)
                savedInstanceState.getSerializable(Keys.searchableSortablePartnerPoints);
    }

    private void restoreClickedMarker(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        LatLng positionOfClickedMarker = readPositionOfClickedMarker(savedInstanceState);
        if (positionOfClickedMarker != null) {
            updateClickedMarkerByPosition(positionOfClickedMarker);
        }
    }

    private static LatLng readPositionOfClickedMarker(Bundle bundle) {
        boolean clickedMarkerExist = bundle.getBoolean(Keys.clickedMarkerExist);
        if (!clickedMarkerExist) {
            return null;
        }
        double latitude = bundle.getDouble(Keys.latitudeOfClickedMarker);
        double longitude = bundle.getDouble(Keys.longitudeOfClickedMarker);
        return new LatLng(latitude, longitude);
    }

    private void updateClickedMarkerByPosition(LatLng position) {
        if (position == null) {
            removeClickedMarker();
            return;
        }
        Marker marker = findMarkerByPosition(position);
        if (marker == null) {
            removeClickedMarker();
            return;
        }
        updateClickedMarker(marker);
    }

    private Marker findMarkerByPosition(LatLng position) {
        for (Marker marker : markers) {
            if (marker.getPosition().equals(position)) {
                return marker;
            }
        }
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.searchableSortablePartnerPoints, searchablePartnerPoints);
        savePositionOfClickedMarker(outState);
    }

    private void savePositionOfClickedMarker(Bundle outState) {
        boolean clickedMarkerExist = (clickedMarker != null);
        outState.putBoolean(Keys.clickedMarkerExist, clickedMarkerExist);
        if (clickedMarkerExist) {
            LatLng positionOfClickedMarker = clickedMarker.getPosition();
            outState.putDouble(Keys.latitudeOfClickedMarker, positionOfClickedMarker.latitude);
            outState.putDouble(Keys.longitudeOfClickedMarker, positionOfClickedMarker.longitude);
        }
    }

    private void updateMap() {
        updateMarkers();
        fitVisibleMarkersOnScreenAfterMapViewWillBePlacedOnLayout();
    }

    private void updateMarkers() {
        getGoogleMap().clear();
        placeMarkersOnMap();
        if (isNeedToHideClickedMarker()) {
            removeClickedMarker();
            onNeedToShowPartnerPointsListener.onNoLongerNeedToShowPartnerPoints();
        }
    }

    private void placeMarkersOnMap() {
        MarkersIncluder markersIncluder = new MarkersIncluder(searchablePartnerPoints);
        markersAndPartnerPoints = markersIncluder.includeIn(getGoogleMap());
        markers = markersAndPartnerPoints.keySet();
    }

    private boolean isNeedToHideClickedMarker() {
        if (clickedMarker == null) {
            return false;
        }
        LatLng positionOfClickedMarker = clickedMarker.getPosition();
        return findMarkerByPosition(positionOfClickedMarker) == null;
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
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Set<PartnerPoint> partnerPointsToShow = markersAndPartnerPoints.get(marker);
        onNeedToShowPartnerPointsListener.onNeedToShowPartnerPoints(partnerPointsToShow);
        updateClickedMarker(marker);
        return true;
    }

    private void updateClickedMarker(Marker marker) {
        if (marker.equals(clickedMarker)) {
            return;
        }
        removeClickedMarker();
        selectMarker(marker);
        clickedMarker = marker;
    }

    private void selectMarker(Marker marker) {
        marker.setRotation(90.0f);
    }

    private void unselectMarker(Marker marker) {
        marker.setRotation(0.0f);
    }

    private void removeClickedMarker() {
        if (clickedMarker == null) {
            return;
        }
        unselectMarker(clickedMarker);
        clickedMarker.setFlat(false);
        clickedMarker = null;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        onMapClickListener.onMapClick(latLng);
        removeClickedMarker();
    }

    public void setOnMapClickListener(GoogleMap.OnMapClickListener listener) {
        onMapClickListener = listener;
    }
}
