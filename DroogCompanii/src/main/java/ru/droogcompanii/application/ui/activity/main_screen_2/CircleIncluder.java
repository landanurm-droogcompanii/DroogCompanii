package ru.droogcompanii.application.ui.activity.main_screen_2;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ls on 18.03.14.
 */
public class CircleIncluder {

    private static final int STROKE_COLOR = 0xff9a2020;
    private static final int FILL_COLOR = 0x77a399a6;

    public static Circle includeIn(GoogleMap googleMap, LatLng center, double radius) {
        return googleMap.addCircle(prepareCircleOptions(center, radius));
    }

    private static CircleOptions prepareCircleOptions(LatLng center, double radius) {
        return new CircleOptions()
                .center(center)
                .radius(radius)
                .strokeColor(STROKE_COLOR)
                .fillColor(FILL_COLOR);
    }
}
