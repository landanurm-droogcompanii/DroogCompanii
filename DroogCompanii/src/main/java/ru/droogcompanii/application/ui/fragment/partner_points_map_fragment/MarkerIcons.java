package ru.droogcompanii.application.ui.fragment.partner_points_map_fragment;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * Created by ls on 27.01.14.
 */
class MarkerIcons {
    private static final float UNSELECTED = BitmapDescriptorFactory.HUE_RED;
    private static final float SELECTED = BitmapDescriptorFactory.HUE_BLUE;

    public static BitmapDescriptor unselected() {
        return BitmapDescriptorFactory.defaultMarker(UNSELECTED);
    }

    public static BitmapDescriptor selected() {
        return BitmapDescriptorFactory.defaultMarker(SELECTED);
    }
}