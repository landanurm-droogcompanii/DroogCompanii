package ru.droogcompanii.application.ui.fragment.partner_points_map;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by ls on 17.03.14.
 */
public class MarkerSelector {

    public static void selectMarker(Marker marker) {
        marker.setIcon(MarkerIcons.selected());
    }

    public static void unselectMarker(Marker marker) {
        marker.setIcon(MarkerIcons.unselected());
    }
}
