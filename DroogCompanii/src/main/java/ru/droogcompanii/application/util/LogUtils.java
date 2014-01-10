package ru.droogcompanii.application.util;

import android.util.Log;

/**
 * Created by ls on 24.12.13.
 */
public class LogUtils {

    public static void debug(String message) {
        Log.d(getTag(), message);
    }

    public static String getTag() {
        return "droogcompanii";
    }
}
