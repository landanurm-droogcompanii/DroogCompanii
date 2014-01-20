package ru.droogcompanii.application;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ls on 20.01.14.
 */
public class DroogCompaniiSharedPreferences {

    public static final String KEY_OF_SHARED_PREFERENCES = DroogCompaniiSharedPreferences.class.getName();

    public static SharedPreferences get(Context context) {
        return context.getSharedPreferences(KEY_OF_SHARED_PREFERENCES, Activity.MODE_PRIVATE);
    }
}
