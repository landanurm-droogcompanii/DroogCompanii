package ru.droogcompanii.application.util.location;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.util.SharedPreferencesUtils;

/**
 * Created by ls on 05.03.14.
 */
public class CustomBaseLocationUtils {

    private static final String KEY_IS_CUSTOM_BASE_LOCATION_SET = getClassName() + "KEY_IS_CUSTOM_BASE_LOCATION_SET";
    private static final String KEY_LATITUDE = getClassName() + "KEY_LATITUDE";
    private static final String KEY_LONGITUDE = getClassName() + "KEY_LONGITUDE";

    private static String getClassName() {
        return CustomBaseLocationUtils.class.getSimpleName();
    }

    public static void init() {
        reset();
    }

    private static void reset() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(KEY_IS_CUSTOM_BASE_LOCATION_SET, false);
        editor.commit();
    }

    private static SharedPreferences getSharedPreferences() {
        Context context = DroogCompaniiApplication.getContext();
        return context.getSharedPreferences(getClassName(), Context.MODE_PRIVATE);
    }

    public static void updateBasePosition(LatLng latLng) {
        setBasePosition(latLng);
        OnLocationChangedListeners.notifyListeners();
    }

    private static void setBasePosition(LatLng position) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(KEY_IS_CUSTOM_BASE_LOCATION_SET, true);
        SharedPreferencesUtils.putDouble(editor, KEY_LATITUDE, position.latitude);
        SharedPreferencesUtils.putDouble(editor, KEY_LONGITUDE, position.longitude);
        editor.commit();
    }

    public static void dismissBasePosition() {
        reset();
        OnLocationChangedListeners.notifyListeners();
    }

    public static boolean isBasePositionSet() {
        return getSharedPreferences().getBoolean(KEY_IS_CUSTOM_BASE_LOCATION_SET, false);
    }

    public static LatLng getBasePosition() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        double latitude = SharedPreferencesUtils.getDouble(sharedPreferences, KEY_LATITUDE, 0.0);
        double longitude = SharedPreferencesUtils.getDouble(sharedPreferences, KEY_LONGITUDE, 0.0);
        return new LatLng(latitude, longitude);
    }

    public static Location getBaseLocation() {
        return LocationConverter.fromLatLng("Custom Base Location", getBasePosition());
    }

}
