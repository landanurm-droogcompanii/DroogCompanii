package ru.droogcompanii.application.global_flags;

import android.content.Context;
import android.content.SharedPreferences;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 31.01.14.
 */
public class VerifierDataForRelevance {

    private static final String PREFERENCES_NAME = "DataIsUpdatedPreferences";

    public static void setDataIsUpdated() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(Keys.dataIsUpdated, true);
        editor.commit();
    }

    private static SharedPreferences getSharedPreferences() {
        Context context = DroogCompaniiApplication.getContext();
        return context.getSharedPreferences(PREFERENCES_NAME, 0);
    }

    public static boolean isDataUpdated() {
        return getSharedPreferences().getBoolean(Keys.dataIsUpdated, false);
    }
}
