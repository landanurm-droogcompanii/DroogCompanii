package ru.droogcompanii.application.ui.fragment.partner_points_map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by ls on 28.01.14.
 */
interface MarkersFinder {
    boolean isMarkerPlacedOnMap(Marker marker);
    Marker findMarkerByPosition(LatLng position);
}
