package ru.droogcompanii.application.ui.activity.able_to_start_task;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import java.io.Serializable;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 21.02.14.
 */
public class ActivityAbleToStartTask
        extends ActionBarActivity implements AbleToStartTask, TaskFragmentHolder.Callbacks {

    private static final String TAG_TASK_FRAGMENT_HOLDER = "TAG_TASK_FRAGMENT_HOLDER";

    private static final String KEY_REQUEST_CODE_OF_RUNNING_TASK = "KEY_REQUEST_CODE_OF_RUNNING_TASK";
    private static final String KEY_IS_RUNNING_TASK = "KEY_IS_RUNNING_TASK";


    private boolean isRunningTask;
    private int requestCodeOfCurrentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            isRunningTask = false;
        } else {
            isRunningTask = savedInstanceState.getBoolean(KEY_IS_RUNNING_TASK);
            requestCodeOfCurrentTask = savedInstanceState.getInt(KEY_REQUEST_CODE_OF_RUNNING_TASK);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_REQUEST_CODE_OF_RUNNING_TASK, requestCodeOfCurrentTask);
        outState.putBoolean(KEY_IS_RUNNING_TASK, isRunningTask);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void startTask(int requestCode, TaskNotBeInterrupted task) {
        startTask(requestCode, task, TaskFragment.NO_TITLE_ID);
    }

    @Override
    public void startTask(int requestCode, TaskNotBeInterrupted task, Integer titleId) {

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

        onReceiveResult(requestCodeOfCurrentTask, resultCode, result);
    }

    protected boolean isRunningTask() {
        return isRunningTask;
    }

    protected void onReceiveResult(int requestCode, int resultCode, Serializable result) {
        // Inheritors should override this method
    }
}
