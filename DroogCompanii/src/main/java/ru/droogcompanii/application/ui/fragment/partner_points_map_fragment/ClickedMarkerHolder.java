package ru.droogcompanii.application.ui.fragment.partner_points_map_fragment;

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
    private LatLng positionOfClickedMarker;

    public ClickedMarkerHolder(MarkersFinder markersFinder) {
        this.markersFinder = markersFinder;
        this.clickedMarker = null;
        this.positionOfClickedMarker = null;
    }

    public void restoreFromIfNeed(Bundle bundle) {
        if (needToRestore(bundle)) {
            restoreFrom(bundle);
        }
    }

    private boolean needToRestore(Bundle bundle) {
        return (bundle != null && bundle.getBoolean(Keys.clickedMarkerIsExist));
    }

    private void restoreFrom(Bundle bundle) {
        positionOfClickedMarker = readPosition(bundle);
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
            savePosition(outState, positionOfClickedMarker);
        }
    }

    private static void savePosition(Bundle bundle, LatLng position) {
        bundle.putDouble(Keys.latitude, position.latitude);
        bundle.putDouble(Keys.longitude, position.longitude);
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
        selectMarker(marker);
        this.clickedMarker = marker;
        this.positionOfClickedMarker = marker.getPosition();
    }

    private static void selectMarker(Marker marker) {
        marker.setIcon(MarkerIcons.selected());
    }

    public void unset() {
        if (isOwnerOfMarker() && markersFinder.isMarkerPlacedOnMap(getMarker())) {
            unselectMarker(getMarker());
        }
        clickedMarker = null;
        positionOfClickedMarker = null;
    }

    private static void unselectMarker(Marker marker) {
        marker.setIcon(MarkerIcons.unselected());
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
}
