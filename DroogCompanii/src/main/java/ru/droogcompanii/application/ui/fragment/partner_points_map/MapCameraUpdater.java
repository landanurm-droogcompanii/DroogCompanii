package ru.droogcompanii.application.ui.fragment.partner_points_map;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.util.Collection;

import ru.droogcompanii.application.util.latlng_bounds_calculator.LatLngBoundsCalculator;

/**
 * Created by ls on 30.01.14.
 */
class MapCameraUpdater {

    public static void fitVisibleMarkers(GoogleMap googleMap, Collection<Marker> markers, int mapPadding) {
        if (isThereNoVisibleMarkers(markers)) {
            return;
        }
        LatLngBounds bounds = LatLngBoundsCalculator.calculateBoundsOfVisibleMarkers(markers);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, mapPadding);
        googleMap.moveCamera(cameraUpdate);
    }

    private static boolean isThereNoVisibleMarkers(Collection<Marker> markers) {
        for (Marker marker : markers) {
            if (marker.isVisible()) {
                return false;
            }
        }
        return true;
    }
}
