package ru.droogcompanii.application.ui.main_screen_2.filters.filters_impl;

import android.content.Context;
import android.content.SharedPreferences;

import ru.droogcompanii.application.util.SharedPreferencesProvider;

/**
 * Created by ls on 25.03.14.
 */
public class FiltersSharedPreferencesProvider {
    public static SharedPreferences get(Context context) {
        return SharedPreferencesProvider.get(context);
    }
}
