package ru.droogcompanii.application.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import ru.droogcompanii.application.DroogCompaniiApplication;

/**
 * Created by ls on 20.01.14.
 */
public class SharedPreferencesProvider {

    public static final String KEY_OF_COMMON_SHARED_PREFERENCES = SharedPreferencesProvider.class.getName();

    public static SharedPreferences get(Context context) {
        return get(context, KEY_OF_COMMON_SHARED_PREFERENCES);
    }

    public static SharedPreferences get(Context context, String key) {
        return context.getSharedPreferences(key, Activity.MODE_PRIVATE);
    }

    public static SharedPreferences getFromApplicationContext() {
        return get(DroogCompaniiApplication.getContext());
    }
}
