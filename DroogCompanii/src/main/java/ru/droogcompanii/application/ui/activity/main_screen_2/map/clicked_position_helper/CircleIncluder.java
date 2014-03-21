package ru.droogcompanii.application.ui.activity.main_screen_2.map.clicked_position_helper;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ls on 18.03.14.
 */
class CircleIncluder {

    public static Circle includeIn(GoogleMap googleMap, LatLng center, double radius) {
        return googleMap.addCircle(prepareCircleOptions(center, radius));
    }

    private static CircleOptions prepareCircleOptions(LatLng center, double radius) {
        return new CircleOptions()
                .center(center)
                .radius(radius)
                .strokeColor(ClickedPositionCircleProperties.STROKE_COLOR)
                .fillColor(ClickedPositionCircleProperties.FILL_COLOR);
    }
}
