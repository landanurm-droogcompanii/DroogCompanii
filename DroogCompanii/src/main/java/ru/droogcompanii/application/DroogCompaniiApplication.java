package ru.droogcompanii.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by ls on 16.01.14.
 */
public class DroogCompaniiApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return DroogCompaniiApplication.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DroogCompaniiApplication.context = getApplicationContext();
    }
}
