package ru.droogcompanii.application.ui.activity.synchronization_activity;

import android.os.Bundle;

import ru.droogcompanii.application.ui.helpers.task.Task;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;

/**
 * Created by ls on 26.12.13.
 */
public class SynchronizationTaskFragmentHolder extends TaskFragmentHolder {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isActivityFirstLaunched()) {
            startTask();
        }
    }

    private boolean isActivityFirstLaunched() {
        SynchronizationActivity activity = (SynchronizationActivity) getActivity();
        return !activity.screenRotated();
    }

    @Override
    protected String getTaskDialogTitle() {
        return "Data is updated...";
    }

    @Override
    protected Task prepareTask() {
        return new SynchronizationTask(getActivity());
    }

}
