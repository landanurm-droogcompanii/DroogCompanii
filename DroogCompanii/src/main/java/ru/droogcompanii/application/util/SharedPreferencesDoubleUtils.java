package ru.droogcompanii.application.util;

import android.content.SharedPreferences;

/**
 * Created by ls on 05.03.14.
 */
public class SharedPreferencesDoubleUtils {

    public static SharedPreferences.Editor putDouble(SharedPreferences.Editor edit, String key, double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    public static double getDouble(SharedPreferences prefs, String key, double defaultValue) {
        if (!prefs.contains(key)) {
            return defaultValue;
        }
        return Double.longBitsToDouble(prefs.getLong(key, 0L));
    }
}
