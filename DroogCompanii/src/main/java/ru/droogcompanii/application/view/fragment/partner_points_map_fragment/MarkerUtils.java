package ru.droogcompanii.application.view.fragment.partner_points_map_fragment;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by ls on 27.01.14.
 */
public class MarkerUtils {

    public static void select(Marker marker) {
        marker.setIcon(MarkerIcons.selected());
    }

    public static void unselect(Marker marker) {
        marker.setIcon(MarkerIcons.unselected());
    }
}
