package ru.droogcompanii.application.ui.fragment.partner_points_map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by ls on 12.03.14.
 */
public class MarkerOptionsPreparer {

    public static MarkerOptions prepare(LatLng position) {
        return new MarkerOptions()
                .position(position)
                .icon(MarkerIcons.unselected());
    }
}
