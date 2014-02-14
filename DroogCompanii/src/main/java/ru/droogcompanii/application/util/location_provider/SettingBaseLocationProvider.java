package ru.droogcompanii.application.util.location_provider;

import android.location.Location;

import java.io.Serializable;

import ru.droogcompanii.application.DroogCompaniiSettings;

/**
 * Created by ls on 14.02.14.
 */
public class SettingBaseLocationProvider implements BaseLocationProvider, Serializable {
    @Override
    public Location getBaseLocation() {
        return DroogCompaniiSettings.getBaseLocation();
    }
}
