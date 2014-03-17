package ru.droogcompanii.application.ui.activity.main_screen_2;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import ru.droogcompanii.application.ui.fragment.partner_points_map.CustomMapFragment;

/**
 * Created by ls on 17.03.14.
 */
public class MapCameraStateSaverLoader {
    private static final String KEY_CENTER = MapCameraStateSaverLoader.class.getName() + "KEY_CENTER";
    private static final String KEY_ZOOM = MapCameraStateSaverLoader.class.getName() + "KEY_ZOOM";

    private final CustomMapFragment customMapFragment;

    public MapCameraStateSaverLoader(CustomMapFragment customMapFragment) {
        this.customMapFragment = customMapFragment;
    }

    public void restoreFrom(Bundle savedInstanceState) {
        LatLng center = savedInstanceState.getParcelable(KEY_CENTER);
        float zoom = savedInstanceState.getFloat(KEY_ZOOM);
        customMapFragment.moveCamera(center, zoom);
    }

    public void saveInto(Bundle outState) {
        outState.putParcelable(KEY_CENTER, customMapFragment.getCurrentCenter());
        outState.putFloat(KEY_ZOOM, customMapFragment.getCurrentZoom());
    }
}
