package ru.droogcompanii.application.ui.activity.main_screen_2.map.clicked_position_helper;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.base.Optional;

import ru.droogcompanii.application.ui.fragment.partner_points_map.CustomMapFragment;

/**
 * Created by ls on 18.03.14.
 */
class CircleDrawer implements ClickedPositionDrawer {

    private final CustomMapFragment mapFragment;
    private Optional<Circle> circle;

    public CircleDrawer(CustomMapFragment mapFragment) {
        this.mapFragment = mapFragment;
        this.circle = Optional.absent();
    }

    @Override
    public void draw(LatLng center) {
        CircleOptions circleOptions = new CircleOptions()
                .center(center)
                .radius(defineRadius())
                .strokeColor(ClickedPositionCircleProperties.STROKE_COLOR)
                .fillColor(ClickedPositionCircleProperties.FILL_COLOR);
        circle = Optional.of(mapFragment.getGoogleMap().addCircle(circleOptions));
    }

    private double defineRadius() {
        return ClickedCircleRadiusCalculator.calculate(mapFragment.getCurrentZoom());
    }

    @Override
    public void remove() {
        circle.get().remove();
        circle = Optional.absent();
    }

    @Override
    public void updateOnCameraChange() {
        if (circle.isPresent()) {
            circle.get().setRadius(defineRadius());
        }
    }

}
