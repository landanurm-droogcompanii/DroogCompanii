package ru.droogcompanii.application.global_flags;

import android.content.Context;
import android.content.SharedPreferences;

import ru.droogcompanii.application.DroogCompaniiApplication;

/**
 * Created by ls on 31.01.14.
 */
public class VerifierDataForRelevance {

    private static final String PREFERENCES_NAME = "DataIsUpdatedPreferences";

    private static final String KEY_IS_DATA_UPDATED = "KEY_IS_DATA_UPDATED";


    public static void setDataIsUpdated() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(KEY_IS_DATA_UPDATED, true);
        editor.commit();
    }

    private static SharedPreferences getSharedPreferences() {
        Context context = DroogCompaniiApplication.getContext();
        return context.getSharedPreferences(PREFERENCES_NAME, 0);
    }

    public static boolean isDataUpdated() {
        return getSharedPreferences().getBoolean(KEY_IS_DATA_UPDATED, false);
    }
}
