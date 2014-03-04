package ru.droogcompanii.application.ui.util;

import android.location.Location;

import java.io.Serializable;

import ru.droogcompanii.application.util.BaseLocationProvider;
import ru.droogcompanii.application.util.CurrentLocationProvider;

/**
 * Created by ls on 14.02.14.
 */
public class CurrentOrDefaultBaseLocationProvider implements BaseLocationProvider, Serializable {
    @Override
    public Location getBaseLocation() {
        return CurrentLocationProvider.getCurrentOrDefaultLocation();
    }
}
