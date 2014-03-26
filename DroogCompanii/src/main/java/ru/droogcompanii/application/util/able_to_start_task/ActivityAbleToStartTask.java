package ru.droogcompanii.application.util.able_to_start_task;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.activity.ActivityTrackedLifecycle;

/**
 * Created by ls on 21.02.14.
 */
public class ActivityAbleToStartTask extends ActivityTrackedLifecycle
        implements AbleToStartTask, TaskResultReceiver, FragmentStartedTaskOnCreate.Callbacks {

    private static final String TAG_TASK_FRAGMENT_HOLDER = "TAG_TASK_FRAGMENT_HOLDER";

    private static final String KEY_REQUEST_CODE_OF_RUNNING_TASK = "KEY_REQUEST_CODE_OF_RUNNING_TASK";
    private static final String KEY_IS_RUNNING_TASK = "KEY_IS_RUNNING_TASK";


    private boolean isRunningTask;
    private int requestCodeOfCurrentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreState(savedInstanceState);
        }
    }

    private void initStateByDefault() {
        isRunningTask = false;
    }

    private void restoreState(Bundle savedInstanceState) {
        isRunningTask = savedInstanceState.getBoolean(KEY_IS_RUNNING_TASK);
        requestCodeOfCurrentTask = savedInstanceState.getInt(KEY_REQUEST_CODE_OF_RUNNING_TASK);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveStateInto(outState);
        super.onSaveInstanceState(outState);
    }

    private void saveStateInto(Bundle outState) {
        outState.putInt(KEY_REQUEST_CODE_OF_RUNNING_TASK, requestCodeOfCurrentTask);
        outState.putBoolean(KEY_IS_RUNNING_TASK, isRunningTask);
    }

    @Override
    public void startTask(int requestCode, TaskNotBeInterruptedDuringConfigurationChange task) {
        startTask(requestCode, task, TaskFragment.NO_TITLE_ID);
    }

    @Override
    public void startTask(int requestCode, TaskNotBeInterruptedDuringConfigurationChange task, Integer titleId) {

        if (isRunningTask) {
            throw new IllegalStateException(
                    "Another task is executing. Might execute only one task at the same time."
            );
        }

        isRunningTask = true;
        requestCodeOfCurrentTask = requestCode;

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        FragmentStartedTaskOnCreate taskFragment = new FragmentStartedTaskOnCreate();
        taskFragment.set(task, titleId);

        if (fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT_HOLDER) != null) {
            transaction.replace(getIdOfTaskFragmentContainer(), taskFragment, TAG_TASK_FRAGMENT_HOLDER);
        } else {
            transaction.add(getIdOfTaskFragmentContainer(), taskFragment, TAG_TASK_FRAGMENT_HOLDER);
        }
        transaction.commitAllowingStateLoss();
    }

    protected int getIdOfTaskFragmentContainer() {
        return R.id.taskFragmentContainer;
    }

    @Override
    public final void onTaskFinished(int resultCode, Serializable result) {

        isRunningTask = false;

        onTaskResult(requestCodeOfCurrentTask, resultCode, result);
    }

    public boolean isRunningTask() {
        return isRunningTask;
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
        // Inheritors should override this method
    }
}
