package ru.droogcompanii.application.ui.activity.synchronization;

import android.os.Bundle;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.helpers.task.Task;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;

/**
 * Created by ls on 26.12.13.
 */
public class SynchronizationTaskFragmentHolder extends TaskFragmentHolder {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            startTask();
        }
    }

    @Override
    protected Integer getTaskDialogTitleId() {
        return R.string.titleOfSynchronizationDialog;
    }

    @Override
    protected Task prepareTask() {
        return new SynchronizationTask(getActivity());
    }

}
