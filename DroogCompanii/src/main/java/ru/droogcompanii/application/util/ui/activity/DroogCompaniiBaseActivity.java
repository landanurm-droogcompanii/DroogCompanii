package ru.droogcompanii.application.util.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;

import java.util.Map;

import ru.droogcompanii.application.R;


/**
 * Created by ls on 26.03.14.
 */
public class DroogCompaniiBaseActivity extends ActionBarActivity {

    public void refresh() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }


    @Override
    public void onStart() {
        super.onStart();

        EasyTracker tracker = EasyTracker.getInstance(this);
        tracker.activityStart(this);

        String trackingId = getString(R.string.ga_trackingId);
        tracker.set(Fields.TRACKING_ID, trackingId);

        String category = "Activity Lifecycle Callback";
        String action = "onStart()";
        String label = "Screen: " + getScreenName();

        Map<String, String> params = MapBuilder.createAppView().createEvent(category, action, label, 0L).build();

        tracker.send(params);
    }

    private String getScreenName() {
        return getClass().getSimpleName();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }
}
