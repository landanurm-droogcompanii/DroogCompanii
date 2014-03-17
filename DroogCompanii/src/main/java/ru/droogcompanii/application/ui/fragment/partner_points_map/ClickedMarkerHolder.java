package ru.droogcompanii.application.ui.fragment.partner_points_map;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by ls on 28.01.14.
 */
class ClickedMarkerHolder {

    private static final String KEY_LATITUDE = "KEY_LATITUDE";
    private static final String KEY_LONGITUDE = "KEY_LONGITUDE";
    private static final String KEY_IS_CLICKED_MARKER_EXIST = "KEY_IS_CLICKED_MARKER_EXIST";

    private final MarkersFinder markersFinder;
    private Marker clickedMarker;
    private LatLng positionOfClickedMarker;

    public ClickedMarkerHolder(MarkersFinder markersFinder) {
        this.markersFinder = markersFinder;
        this.clickedMarker = null;
        this.positionOfClickedMarker = null;
    }

    public void restoreFrom(Bundle bundle) {
        boolean isClickedMarkerExist = bundle.getBoolean(KEY_IS_CLICKED_MARKER_EXIST);
        if (!isClickedMarkerExist) {
            return;
        }
        positionOfClickedMarker = readPosition(bundle);
    }

    private static LatLng readPosition(Bundle bundle) {
        double latitude = bundle.getDouble(KEY_LATITUDE);
        double longitude = bundle.getDouble(KEY_LONGITUDE);
        return new LatLng(latitude, longitude);
    }

    public void saveInto(Bundle outState) {
        if (isAbsent()) {
            outState.putBoolean(KEY_IS_CLICKED_MARKER_EXIST, false);
        } else {
            outState.putBoolean(KEY_IS_CLICKED_MARKER_EXIST, true);
            savePosition(outState, positionOfClickedMarker);
        }
    }

    private static void savePosition(Bundle bundle, LatLng position) {
        bundle.putDouble(KEY_LATITUDE, position.latitude);
        bundle.putDouble(KEY_LONGITUDE, position.longitude);
    }

    public boolean isAbsent() {
        return (positionOfClickedMarker == null);
    }

    public void update() {
        if (isAbsent()) {
            return;
        }
        update(findActualClickedMarker());
    }

    private void update(Marker newMarker) {
        if (newMarker != null) {
            set(newMarker);
        } else {
            unset();
        }
    }

    private Marker findActualClickedMarker() {
        return markersFinder.findMarkerByPosition(positionOfClickedMarker);
    }

    public Marker getMarker() {
        if (positionOfClickedMarker == null) {
            return null;
        }
        if (clickedMarker == null) {
            clickedMarker = markersFinder.findMarkerByPosition(positionOfClickedMarker);
        }
        return clickedMarker;
    }

    public void set(Marker marker) {
        unset();
        MarkerSelector.selectMarker(marker);
        this.clickedMarker = marker;
        this.positionOfClickedMarker = marker.getPosition();
    }

    public void unset() {
        if (isOwnerOfMarker() && markersFinder.isMarkerPlacedOnMap(getMarker())) {
            MarkerSelector.unselectMarker(getMarker());
        }
        clickedMarker = null;
        positionOfClickedMarker = null;
    }

    public boolean isHolding(Marker marker) {
        if (isAbsent() || marker == null) {
            return false;
        }
        return positionOfClickedMarker.equals(marker.getPosition());
    }

    public LatLng getPosition() {
        return positionOfClickedMarker;
    }

    public boolean isPresent() {
        return !isAbsent();
    }

    public boolean isOwnerOfMarker() {
        return getMarker() != null;
    }

    public boolean isShowing() {
        return isPresent() && markersFinder.isMarkerPlacedOnMap(clickedMarker) && clickedMarker.isVisible();
    }
}
