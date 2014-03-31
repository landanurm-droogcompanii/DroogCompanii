package ru.droogcompanii.application.util;

import com.google.android.gms.maps.model.LatLng;
import com.google.common.base.Optional;
import com.google.maps.android.SphericalUtil;

import ru.droogcompanii.application.util.location.ActualBaseLocationProvider;

/**
 * Created by Leonid on 16.03.14.
 */
public class NearestPositionCalculator {
    private final LatLng positionOfBaseLocation;

    private double minDistance;
    private LatLng nearestPosition;

    public NearestPositionCalculator(LatLng positionOfBaseLocation) {
        this.positionOfBaseLocation = positionOfBaseLocation;
        this.minDistance = Double.MAX_VALUE;
        this.nearestPosition = null;
    }

    public void add(LatLng position) {
        double distance = SphericalUtil.computeDistanceBetween(position, positionOfBaseLocation);
        if (distance < minDistance) {
            minDistance = distance;
            nearestPosition = position;
        }
    }

    public Optional<SerializableLatLng> getNearestPosition() {
        if (nearestPosition != null) {
            return Optional.of(SerializableLatLng.fromParcelable(nearestPosition));
        } else {
            return Optional.absent();
        }
    }

    public static NearestPositionCalculator fromActualBaseLocation() {
        return new NearestPositionCalculator(ActualBaseLocationProvider.getPositionOfActualBaseLocation());
    }
}