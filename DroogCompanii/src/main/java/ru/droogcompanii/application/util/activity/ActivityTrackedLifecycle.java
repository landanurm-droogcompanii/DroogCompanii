package ru.droogcompanii.application.util.activity;

import android.os.Bundle;

import com.google.common.base.Optional;

/**
 * Created by ls on 06.03.14.
 */
public class ActivityTrackedLifecycle extends DroogCompaniiBaseActivity {

    private Optional<Boolean> isCreated = Optional.absent();
    private Optional<Boolean> isStarted = Optional.absent();
    private Optional<Boolean> isResumed = Optional.absent();

    public boolean isActivityStarted() {
        return isStarted.isPresent() && isStarted.get();
    }

    public boolean isActivityStopped() {
        return isStarted.isPresent() && !isStarted.get();
    }

    public boolean isActivityResumed() {
        return isResumed.isPresent() && isResumed.get();
    }

    public boolean isActivityPaused() {
        return isResumed.isPresent() && !isResumed.get();
    }

    public boolean isActivityCreated() {
        return isCreated.isPresent() && isCreated.get();
    }

    public boolean isActivityDestroyed() {
        return isCreated.isPresent() && !isCreated.get();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isCreated = Optional.of(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isCreated = Optional.of(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isStarted = Optional.of(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isStarted = Optional.of(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = Optional.of(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = Optional.of(false);
    }
}
