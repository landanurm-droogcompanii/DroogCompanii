package ru.droogcompanii.application.util;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

/**
 * Created by ls on 31.03.14.
 */
public class LocationSettingsScreenOpener {

    public static void open(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }
}
