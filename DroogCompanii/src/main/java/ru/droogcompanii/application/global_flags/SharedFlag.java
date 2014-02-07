package ru.droogcompanii.application.global_flags;

import android.content.SharedPreferences;

import ru.droogcompanii.application.util.SharedPreferencesProvider;

/**
 * Created by ls on 03.02.14.
 */
public class SharedFlag {

    private final String key;

    public static SharedFlag from(String key) {
        return new SharedFlag(key);
    }

    private SharedFlag(String key) {
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
