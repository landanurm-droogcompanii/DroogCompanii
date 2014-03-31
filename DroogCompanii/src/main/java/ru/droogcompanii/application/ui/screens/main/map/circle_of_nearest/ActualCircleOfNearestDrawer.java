package ru.droogcompanii.application.ui.screens.main.map.circle_of_nearest;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.base.Optional;

import ru.droogcompanii.application.ui.screens.main.map.CustomMapFragment;
import ru.droogcompanii.application.ui.screens.main.filters_dialog.filters.DistanceFilterHelper;
import ru.droogcompanii.application.util.location.ActualBaseLocationProvider;

/**
 * Created by ls on 25.03.14.
 */
public class ActualCircleOfNearestDrawer implements CircleOfNearestDrawer {
    private final CustomMapFragment mapFragment;
    private Optional<Circle> optionalCircle;

    public ActualCircleOfNearestDrawer(CustomMapFragment mapFragment) {
        this.mapFragment = mapFragment;
        this.optionalCircle = Optional.absent();
    }

    public void update() {
        DistanceFilterHelper distanceFilter = DistanceFilterHelper.getCurrent(mapFragment.getActivity());
        if (distanceFilter.isActive()) {
            double radius = distanceFilter.getMaxRadius();
            drawIfNeed(radius);
        } else {
            removeIfNeed();
        }
    }

    private void drawIfNeed(double radius) {
        LatLng actualCenter = ActualBaseLocationProvider.getPositionOfActualBaseLocation();
        if (optionalCircle.isPresent()) {
            updateCircle(actualCenter, radius);
        } else {
            optionalCircle = Optional.of(drawCircle(actualCenter, radius));
        }
    }

    private void updateCircle(LatLng actualCenter, double radius) {
        Circle circle = optionalCircle.get();
        circle.setRadius(radius);
        circle.setCenter(actualCenter);
        circle.setVisible(true);
    }

    private Circle drawCircle(LatLng center, double radius) {
        CircleOptions circleOptions = new CircleOptions()
                .center(center)
                .radius(radius)
                .strokeWidth(CircleAttributes.STROKE_WIDTH)
                .strokeColor(CircleAttributes.STROKE_COLOR)
                .fillColor(CircleAttributes.FILL_COLOR);
        return mapFragment.getGoogleMap().addCircle(circleOptions);
    }

    private void removeIfNeed() {
        if (optionalCircle.isPresent()) {
            optionalCircle.get().setVisible(false);
        }
    }
}
