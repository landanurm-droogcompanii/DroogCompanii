package ru.droogcompanii.application.ui.util;

import android.location.Location;

import com.google.common.base.Optional;

import java.io.Serializable;

import ru.droogcompanii.application.DroogCompaniiSettings;
import ru.droogcompanii.application.util.BaseLocationProvider;

/**
 * Created by ls on 14.02.14.
 */
public class SettingBaseLocationProvider implements BaseLocationProvider, Serializable {
    @Override
    public Optional<Location> getBaseLocation() {
        return Optional.of(DroogCompaniiSettings.getBaseLocation());
    }
}
