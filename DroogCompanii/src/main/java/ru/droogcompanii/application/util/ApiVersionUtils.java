package ru.droogcompanii.application.util;

/**
 * Created by ls on 17.02.14.
 */
public class ApiVersionUtils {

    public static boolean isCurrentVersionLowerThan(int apiVersion) {
        return android.os.Build.VERSION.SDK_INT < apiVersion;
    }
}
