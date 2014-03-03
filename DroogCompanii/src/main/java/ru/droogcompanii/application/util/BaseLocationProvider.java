package ru.droogcompanii.application.util;

import android.location.Location;

import com.google.common.base.Optional;

/**
 * Created by ls on 30.01.14.
 */
public interface BaseLocationProvider {
    Optional<Location> getBaseLocation();
}
