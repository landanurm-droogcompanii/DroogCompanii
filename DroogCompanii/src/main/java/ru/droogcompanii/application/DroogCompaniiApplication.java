package ru.droogcompanii.application;

import android.app.Application;
import android.content.Context;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

/**
 * Created by ls on 16.01.14.
 */
public class DroogCompaniiApplication extends Application {

    private static Context context;
    private static GoogleAnalytics mGa;
    private static Tracker mTracker;

    public static Context getContext() {
        return DroogCompaniiApplication.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DroogCompaniiApplication.context = getApplicationContext();

        //initializeGa();
    }

    private void initializeGa() {
        String trackingId = getApplicationContext().getString(R.string.ga_trackingId);

        mGa = GoogleAnalytics.getInstance(this);
        mTracker = mGa.getTracker(trackingId);

    }

    public static Tracker getGaTracker() {
        return mTracker;
    }

    public static GoogleAnalytics getGaInstance() {
        return mGa;
    }
}
