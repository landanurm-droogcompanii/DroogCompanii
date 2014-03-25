package ru.droogcompanii.application.ui.activity.main_screen_2.map.clicked_position_helper;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ls on 25.03.14.
 */
interface ClickedPositionDrawer {
    void draw(LatLng center);
    void remove();
    void updateOnCameraChange();
}
