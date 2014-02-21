package ru.droogcompanii.application.ui.activity.synchronization;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.helpers.task.TaskNotBeInterrupted;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;

/**
 * Created by ls on 26.12.13.
 */
public class SynchronizationTaskFragmentHolder extends TaskFragmentHolder {

    @Override
    protected Integer getTaskDialogTitleId() {
        return R.string.titleOfSynchronizationDialog;
    }

    @Override
    protected TaskNotBeInterrupted prepareTask() {
        return new SynchronizationTask(getActivity());
    }

}
