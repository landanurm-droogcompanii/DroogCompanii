package ru.droogcompanii.application.util;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Collection;

public class LatLngBoundsCalculator {
    private static final double RIGHT_UPPER_LATITUDE = 55.800361;
    private static final double RIGHT_UPPER_LONGITUDE = 49.192142;

    private static final double LEFT_DOWN_LATITUDE = 55.738413;
    private static final double LEFT_DOWN_LONGITUDE = 49.122963;

    private static final LatLng RIGHT_UPPER = new LatLng(RIGHT_UPPER_LATITUDE, RIGHT_UPPER_LONGITUDE);
    private static final LatLng LEFT_DOWN = new LatLng(LEFT_DOWN_LATITUDE, LEFT_DOWN_LONGITUDE);


    private final Collection<Marker> markers;
    private LatLngBounds.Builder builder;


    public static LatLngBounds calculateBoundsOfVisibleMarkers(Collection<Marker> markers) {
        return new LatLngBoundsCalculator(markers).calculateBoundsOfVisibleMarkers();
    }

    public LatLngBoundsCalculator(Collection<Marker> markers) {
        this.markers = new ArrayList<Marker>(markers);
    }

    public LatLngBounds calculateBoundsOfVisibleMarkers() {
        builder = new LatLngBounds.Builder();
        for (Marker each : markers) {
            if (each.isVisible()) {
                builder.include(each.getPosition());
            }
        }
        includeMinBounds();
        return builder.build();
    }

    private void includeMinBounds() {
        builder.include(LEFT_DOWN);
        builder.include(RIGHT_UPPER);
    }

}
