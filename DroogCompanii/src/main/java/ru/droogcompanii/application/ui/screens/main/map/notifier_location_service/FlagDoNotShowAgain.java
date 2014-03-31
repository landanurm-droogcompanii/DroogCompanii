package ru.droogcompanii.application.ui.screens.main.map.notifier_location_service;

import android.content.Context;
import android.content.SharedPreferences;

import ru.droogcompanii.application.util.SharedPreferencesProvider;

/**
 * Created by ls on 31.03.14.
 */
public class FlagDoNotShowAgain {
    private static final boolean DEFAULT_DO_NOT_SHOW_AGAIN = false;
    private static final String KEY_DO_NOT_SHOW_AGAIN = FlagDoNotShowAgain.class.getName();

    public static void share(Context context, boolean doNotShowAgain) {
        SharedPreferences sharedPreferences = SharedPreferencesProvider.get(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_DO_NOT_SHOW_AGAIN, doNotShowAgain);
        editor.commit();
    }

    public static boolean read(Context context) {
        SharedPreferences sharedPreferences = SharedPreferencesProvider.get(context);
        return sharedPreferences.getBoolean(KEY_DO_NOT_SHOW_AGAIN, DEFAULT_DO_NOT_SHOW_AGAIN);
    }
}