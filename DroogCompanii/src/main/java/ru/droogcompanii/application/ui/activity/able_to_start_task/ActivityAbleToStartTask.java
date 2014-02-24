package ru.droogcompanii.application.ui.activity.able_to_start_task;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.helpers.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.ui.helpers.FragmentRemover;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;
import ru.droogcompanii.application.ui.helpers.task.TaskNotBeInterrupted;

/**
 * Created by ls on 21.02.14.
 */
public abstract class ActivityAbleToStartTask
        extends ActionBarActivityWithUpButton
        implements AbleToStartTask, TaskFragmentHolder.Callbacks {

    private static final String TAG_TASK_FRAGMENT_HOLDER = "TAG_TASK_FRAGMENT_HOLDER";

    @Override
    public void startTask(TaskNotBeInterrupted task) {
        startTask(task, null);
    }

    @Override
    public void startTask(TaskNotBeInterrupted task, Integer titleId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        CommonTaskFragmentHolder taskFragment = new CommonTaskFragmentHolder();
        taskFragment.set(task, titleId);
        transaction.add(getIdOfTaskFragmentContainer(), taskFragment, TAG_TASK_FRAGMENT_HOLDER);
        transaction.commit();
    }

    protected int getIdOfTaskFragmentContainer() {
        return R.id.taskFragmentContainer;
    }

    @Override
    public void onTaskFinished(int resultCode, Serializable result) {
        FragmentRemover.removeFragmentByTag(this, TAG_TASK_FRAGMENT_HOLDER);
        getResultReceiver().onTaskResult(resultCode, result);
    }

    private TaskResultReceiver getResultReceiver() {
        String tag = getTagOfTaskResultReceiverFragment();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        return (TaskResultReceiver) fragment;
    }

    protected abstract String getTagOfTaskResultReceiverFragment();
}
