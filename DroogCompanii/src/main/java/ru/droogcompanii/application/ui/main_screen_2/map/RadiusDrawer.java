package ru.droogcompanii.application.ui.main_screen_2.map;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.base.Optional;

import ru.droogcompanii.application.ui.fragment.partner_points_map.CustomMapFragment;
import ru.droogcompanii.application.ui.main_screen_2.filters.filters_impl.DistanceFilter;
import ru.droogcompanii.application.ui.util.ActualBaseLocationProvider;

/**
 * Created by ls on 25.03.14.
 */
class RadiusDrawer {

    private static final int BASE_COLOR = 0x003366dd;

    private static final int FILL_COLOR = 0x58000000 | BASE_COLOR;
    private static final int STROKE_COLOR = 0xbb000000 | BASE_COLOR;

    private static final float STROKE_WIDTH = 2.0f;


    private final CustomMapFragment mapFragment;
    private Optional<Circle> circle;

    public RadiusDrawer(CustomMapFragment mapFragment) {
        this.mapFragment = mapFragment;
        this.circle = Optional.absent();
    }

    public void update() {
        DistanceFilter distanceFilter = DistanceFilter.getCurrent(mapFragment.getActivity());
        if (distanceFilter.isActivated()) {
            drawIfNeed(distanceFilter.getMaxRadius());
        } else {
            removeIfNeed();
        }
    }

    private void drawIfNeed(double radius) {
        LatLng actualCenter = ActualBaseLocationProvider.getPositionOfActualBaseLocation();
        if (circle.isPresent()) {
            Circle oldCircle = circle.get();
            if (oldCircle.getRadius() == radius && actualCenter.equals(oldCircle.getCenter())) {
                return;
            }
            oldCircle.remove();
        }
        circle = draw(actualCenter, radius);
    }

    private Optional<Circle> draw(LatLng center, double radius) {
        CircleOptions circleOptions = new CircleOptions()
                .center(center)
                .radius(radius)
                .strokeWidth(STROKE_WIDTH)
                .strokeColor(STROKE_COLOR)
                .fillColor(FILL_COLOR);
        Circle circle = mapFragment.getGoogleMap().addCircle(circleOptions);
        return Optional.of(circle);
    }

    private void removeIfNeed() {
        if (circle.isPresent()) {
            circle.get().remove();
            circle = Optional.absent();
        }
    }
}
