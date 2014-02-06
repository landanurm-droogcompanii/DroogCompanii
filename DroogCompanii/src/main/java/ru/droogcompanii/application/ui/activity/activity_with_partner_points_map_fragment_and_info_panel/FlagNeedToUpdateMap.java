package ru.droogcompanii.application.ui.activity.activity_with_partner_points_map_fragment_and_info_panel;

import android.content.Context;
import android.content.SharedPreferences;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.util.SharedPreferencesProvider;

/**
 * Created by ls on 06.02.14.
 */
public class FlagNeedToUpdateMap {
    private static final String KEY = FlagNeedToUpdateMap.class.getName();
    private static final boolean DEFAULT = false;

    public static boolean isSet() {
        return getSharedPreferences().getBoolean(KEY, DEFAULT);
    }

    private static SharedPreferences getSharedPreferences() {
        Context context = DroogCompaniiApplication.getContext();
        return SharedPreferencesProvider.get(context);
    }

    public static void set(boolean needToUpdate) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(KEY, needToUpdate);
        editor.commit();
    }

    public static void init() {
        set(DEFAULT);
    }
}
