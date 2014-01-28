package ru.droogcompanii.application.view.fragment.partner_points_map_fragment;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 28.01.14.
 */
class ClickedMarkerHolder {
    private final MarkersFinder markersFinder;
    private Marker clickedMarker;

    public ClickedMarkerHolder(MarkersFinder markersFinder) {
        this.markersFinder = markersFinder;
        this.clickedMarker = null;
    }

    public void restoreFromIfNeed(Bundle bundle) {
        if (needToRestore(bundle)) {
            restoreFrom(bundle);
        }
    }

    private static boolean needToRestore(Bundle bundle) {
        return (bundle != null) && bundle.getBoolean(Keys.clickedMarkerIsExist);
    }

    private void restoreFrom(Bundle bundle) {
        LatLng positionOfClickedMarker = readPosition(bundle);
        Marker marker = markersFinder.findMarkerByPosition(positionOfClickedMarker);
        update(marker);
    }

    private void update(Marker newMarker) {
        if (newMarker != null) {
            set(newMarker);
        } else {
            unset();
        }
    }

    private static LatLng readPosition(Bundle bundle) {
        double latitude = bundle.getDouble(Keys.latitude);
        double longitude = bundle.getDouble(Keys.longitude);
        return new LatLng(latitude, longitude);
    }

    public void saveInto(Bundle outState) {
        if (isAbsent()) {
            outState.putBoolean(Keys.clickedMarkerIsExist, false);
        } else {
            outState.putBoolean(Keys.clickedMarkerIsExist, true);
            savePosition(outState, clickedMarker.getPosition());
        }
    }

    private static void savePosition(Bundle bundle, LatLng position) {
        bundle.putDouble(Keys.latitude, position.latitude);
        bundle.putDouble(Keys.longitude, position.longitude);
    }

    public boolean isAbsent() {
        return (clickedMarker == null);
    }

    public void update() {
        if (isAbsent()) {
            return;
        }
        update(findActualClickedMarker());
    }

    private Marker findActualClickedMarker() {
        LatLng position = clickedMarker.getPosition();
        return markersFinder.findMarkerByPosition(position);
    }

    public Marker getMarker() {
        return clickedMarker;
    }

    public void set(Marker marker) {
        unset();
        selectMarker(marker);
        this.clickedMarker = marker;
    }

    private static void selectMarker(Marker marker) {
        marker.setIcon(MarkerIcons.selected());
    }

    public void unset() {
        if (isAbsent()) {
            return;
        }
        if (markersFinder.isMarkerPlacedOnMap(clickedMarker)) {
            unselectMarker(clickedMarker);
        }
        clickedMarker = null;
    }

    private static void unselectMarker(Marker marker) {
        marker.setIcon(MarkerIcons.unselected());
    }
}
