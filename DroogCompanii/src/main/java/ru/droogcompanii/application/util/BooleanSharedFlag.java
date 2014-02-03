package ru.droogcompanii.application.util;

import android.content.SharedPreferences;

/**
 * Created by ls on 03.02.14.
 */
public class BooleanSharedFlag {

    private final String key;

    public static BooleanSharedFlag from(String key) {
        return new BooleanSharedFlag(key);
    }

    private BooleanSharedFlag(String key) {
        this.key = key;
    }

    public void set(boolean value) {
        SharedPreferences.Editor editor = SharedPreferencesProvider.getFromApplicationContext().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean get() {
        SharedPreferences sharedPreferences = SharedPreferencesProvider.getFromApplicationContext();
        return sharedPreferences.getBoolean(key, false);
    }
}
