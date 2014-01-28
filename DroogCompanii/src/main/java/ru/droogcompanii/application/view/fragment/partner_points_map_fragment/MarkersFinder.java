package ru.droogcompanii.application.view.fragment.partner_points_map_fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by ls on 28.01.14.
 */
public interface MarkersFinder {
    boolean isMarkerPlacedOnMap(Marker marker);
    Marker findMarkerByPosition(LatLng position);
}
