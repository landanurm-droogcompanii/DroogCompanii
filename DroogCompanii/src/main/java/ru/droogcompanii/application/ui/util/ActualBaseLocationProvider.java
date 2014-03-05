package ru.droogcompanii.application.ui.util;

import android.location.Location;

import java.io.Serializable;

import ru.droogcompanii.application.util.BaseLocationProvider;

/**
 * Created by ls on 14.02.14.
 */
public class ActualBaseLocationProvider implements BaseLocationProvider, Serializable {

    @Override
    public Location getBaseLocation() {
        return LocationUtils.getActualLocation();
    }
}
