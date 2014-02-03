package ru.droogcompanii.application.ui.activity.start_activity;

import android.content.SharedPreferences;

import ru.droogcompanii.application.util.SharedPreferencesProvider;

/**
 * Created by ls on 03.02.14.
 */
public class BooleanSharedPreference {

    private final String key;

    public static BooleanSharedPreference from(String key) {
        return new BooleanSharedPreference(key);
    }

    private BooleanSharedPreference(String key) {
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
