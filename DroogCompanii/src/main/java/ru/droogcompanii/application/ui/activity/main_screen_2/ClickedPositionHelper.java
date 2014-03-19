package ru.droogcompanii.application.ui.activity.main_screen_2;

import android.os.Bundle;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.base.Optional;

import ru.droogcompanii.application.ui.fragment.partner_points_map.CustomMapFragment;

/**
 * Created by ls on 17.03.14.
 */
public class ClickedPositionHelper {

    private static final String KEY_CENTER = "KEY_CENTER";

    private final CustomMapFragment mapFragment;
    private LatLng center;
    private Circle circle;

    public ClickedPositionHelper(CustomMapFragment mapFragment) {
        this.mapFragment = mapFragment;
        this.center = null;
        this.circle = null;
    }

    public void restoreFrom(Bundle savedInstanceState) {
        LatLng restoredCenter = savedInstanceState.getParcelable(KEY_CENTER);
        if (restoredCenter != null) {
            set(restoredCenter);
        }
    }

    public void saveInto(Bundle outState) {
        outState.putParcelable(KEY_CENTER, center);
    }

    public void set(LatLng newCenter) {
        if (center != null) {
            remove();
        }
        center = newCenter;
        draw();
    }

    private void draw() {
        if (circle != null) {
            circle.setVisible(true);
        } else {
            circle = prepareCircle();
        }
    }

    private Circle prepareCircle() {
        return CircleIncluder.includeIn(
                mapFragment.getGoogleMap(), center, defineRadius()
        );
    }

    private double defineRadius() {
        return ClickedCircleRadiusCalculator.calculate(mapFragment.getCurrentZoom());
    }

    public void remove() {
        if (circle != null) {
            circle.remove();
            circle = null;
        }
        center = null;
    }

    public Optional<LatLng> getOptionalClickedPosition() {
        return Optional.fromNullable(center);
    }

    public LatLng getClickedPosition() {
        return center;
    }

    public boolean isClickedPositionPresent() {
        return center != null;
    }

    public void updateOnCameraChange() {
        if (circle != null) {
            circle.setRadius(defineRadius());
        }
    }
}
