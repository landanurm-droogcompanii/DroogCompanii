package ru.droogcompanii.application.ui.activity.able_to_start_task;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.helpers.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.ui.helpers.FragmentRemover;
import ru.droogcompanii.application.ui.helpers.task.TaskFragment;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;
import ru.droogcompanii.application.ui.helpers.task.TaskNotBeInterrupted;
import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by ls on 21.02.14.
 */
public abstract class ActivityAbleToStartTask
        extends ActionBarActivityWithUpButton
        implements AbleToStartTask, TaskFragmentHolder.Callbacks {

    private static final String TAG_TASK_FRAGMENT_HOLDER = "TAG_TASK_FRAGMENT_HOLDER";

    private static final String KEY_REQUEST_CODE = "RequestCode";
    private static final String KEY_IS_RUNNING_TASK = "IsRunningTask";


    private boolean isRunningTask;
    private int requestCodeOfCurrentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            isRunningTask = false;
        } else {
            isRunningTask = savedInstanceState.getBoolean(KEY_IS_RUNNING_TASK);
            requestCodeOfCurrentTask = savedInstanceState.getInt(KEY_REQUEST_CODE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_REQUEST_CODE, requestCodeOfCurrentTask);
        outState.putBoolean(KEY_IS_RUNNING_TASK, isRunningTask);
        super.onSaveInstanceState(outState);
    }

    @Override
    public final void startTask(int requestCode, TaskNotBeInterrupted task) {
        startTask(requestCode, task, TaskFragment.NO_TITLE_ID);
    }

    @Override
    public final void startTask(int requestCode, TaskNotBeInterrupted task, Integer titleId) {
        LogUtils.debug("Task Launched:  " + task.getClass().getSimpleName());

        if (isRunningTask) {
            throw new IllegalStateException(
                    "Another task is executing. Might execute only one task at the same time."
            );
        }

        isRunningTask = true;
        requestCodeOfCurrentTask = requestCode;

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        CommonTaskFragmentHolder taskFragment = new CommonTaskFragmentHolder();
        taskFragment.set(task, titleId);
        transaction.add(getIdOfTaskFragmentContainer(), taskFragment, TAG_TASK_FRAGMENT_HOLDER);
        transaction.commitAllowingStateLoss();
    }

    protected int getIdOfTaskFragmentContainer() {
        return R.id.taskFragmentContainer;
    }

    @Override
    public final void onTaskFinished(int resultCode, Serializable result) {

        LogUtils.debug("Task finished");

        FragmentRemover.removeFragmentByTag(this, TAG_TASK_FRAGMENT_HOLDER);

        isRunningTask = false;

        onReceiveResult(requestCodeOfCurrentTask, resultCode, result);
    }

    protected abstract void onReceiveResult(int requestCode, int resultCode, Serializable result);

    protected boolean isRunningTask() {
        return isRunningTask;
    }
}
