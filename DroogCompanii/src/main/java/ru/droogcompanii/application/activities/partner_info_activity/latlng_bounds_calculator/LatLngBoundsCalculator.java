package ru.droogcompanii.application.activities.partner_info_activity.latlng_bounds_calculator;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class LatLngBoundsCalculator {
    private static final double rightUpperLatitude = 55.800361;
    private static final double rightUpperLongitude = 49.192142;

    private static final double leftDownLatitude = 55.738413;
    private static final double leftDownLongitude = 49.122963;

    private static final double minLatitudeLength = Math.abs(rightUpperLatitude - leftDownLatitude);
    private static final double minLongitudeLength = Math.abs(rightUpperLongitude - leftDownLongitude);

    private static final double halfOfMinLatitudeLength = minLatitudeLength / 2.0;
    private static final double halfOfMinLongitudeLength = minLongitudeLength / 2.0;

    private final LatLngBounds.Builder builder;
    private final List<Marker> markers;
    private final MinMax latitudeMinMax;
    private final MinMax longitudeMinMax;

    private LatLng center;


    public static LatLngBounds calculate(List<Marker> markers) {
        return new LatLngBoundsCalculator(markers).calculate();
    }

    private LatLngBoundsCalculator(List<Marker> markers) {
        this.markers = markers;
        this.latitudeMinMax = new MinMax();
        this.longitudeMinMax = new MinMax();
        this.builder = new LatLngBounds.Builder();
    }

    private LatLngBounds calculate() {
        for (Marker each : markers) {
            if (each.isVisible()) {
                includePosition(each.getPosition());
            }
        }
        includeMinBounds();
        return builder.build();
    }

    private void includePosition(LatLng position) {
        builder.include(position);
        latitudeMinMax.add(position.latitude);
        longitudeMinMax.add(position.longitude);
    }

    private void includeMinBounds() {
        center = calculateCenter();
        MinMax latitude = getMinBoundsLatitude();
        MinMax longitude = getMinBoundsLongitude();
        builder.include(new LatLng(latitude.min(), longitude.min()));
        builder.include(new LatLng(latitude.min(), longitude.max()));
        builder.include(new LatLng(latitude.max(), longitude.min()));
        builder.include(new LatLng(latitude.max(), longitude.max()));
    }

    private LatLng calculateCenter() {
        double centerLatitude = (latitudeMinMax.min() + latitudeMinMax.max()) / 2;
        double centerLongitude = (longitudeMinMax.min() + longitudeMinMax.max()) / 2;
        return new LatLng(centerLatitude, centerLongitude);
    }

    private MinMax getMinBoundsLatitude() {
        MinMax latitude = new MinMax();
        double minLatitude = LatitudeCalculator.subtract(center.latitude, halfOfMinLatitudeLength);
        double maxLatitude = LatitudeCalculator.add(center.latitude, halfOfMinLatitudeLength);
        latitude.add(minLatitude);
        latitude.add(maxLatitude);
        return latitude;
    }

    private MinMax getMinBoundsLongitude() {
        MinMax longitude = new MinMax();
        double minLongitude = LongitudeCalculator.subtract(center.longitude, halfOfMinLongitudeLength);
        double maxLongitude = LongitudeCalculator.add(center.longitude, halfOfMinLongitudeLength);
        longitude.add(minLongitude);
        longitude.add(maxLongitude);
        return longitude;
    }
}
