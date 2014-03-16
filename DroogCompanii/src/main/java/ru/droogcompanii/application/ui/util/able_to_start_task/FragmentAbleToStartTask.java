package ru.droogcompanii.application.ui.util.able_to_start_task;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;

/**
 * Created by ls on 06.03.14.
 */
public abstract class FragmentAbleToStartTask extends Fragment
        implements AbleToStartTask, TaskResultReceiver {

    public static final int TASK_FRAGMENT_TARGET = 123;

    protected static final String TAG_TASK_FRAGMENT = FragmentAbleToStartTask.class.getName() + "TAG_TASK_FRAGMENT";

    private static final String KEY_TASK_REQUEST_CODE = "KEY_TASK_REQUEST_CODE";

    private FragmentManager fragmentManager;
    private int taskRequestCode;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TASK_REQUEST_CODE, taskRequestCode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }

        fragmentManager = getFragmentManager();

        TaskFragment taskFragment = getTaskFragment();
        if (taskFragment != null) {
            taskFragment.setTargetFragment(this, TASK_FRAGMENT_TARGET);
        }
    }

    private void restoreState(Bundle savedInstanceState) {
        taskRequestCode = savedInstanceState.getInt(KEY_TASK_REQUEST_CODE);
    }

    private TaskFragment getTaskFragment() {
        return (TaskFragment) fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);
    }

    public void startTask(int requestCode, TaskNotBeInterruptedDuringConfigurationChange task) {
        startTask(requestCode, task, TaskFragment.NO_TITLE_ID);
    }

    public void startTask(int requestCode, TaskNotBeInterruptedDuringConfigurationChange task, Integer title) {
        this.taskRequestCode = requestCode;
        TaskFragment taskFragment = new TaskFragment();
        taskFragment.setTitle(title);
        taskFragment.setTask(task);
        taskFragment.setTargetFragment(this, TASK_FRAGMENT_TARGET);
        startFragment(taskFragment);
    }

    private void startFragment(TaskFragment taskFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment previousTaskFragment = fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);
        if (previousTaskFragment != null) {
            transaction.remove(previousTaskFragment);
        }
        transaction.add(taskFragment, TAG_TASK_FRAGMENT);
        transaction.commit();
    }

    public final void onResult(int requestCode, int resultCode, Serializable result) {
        if ((requestCode == TASK_FRAGMENT_TARGET)) {
            onTaskResult(this.taskRequestCode, resultCode, result);
        }
    }

    public abstract void onTaskResult(int requestCode, int resultCode, Serializable result);

    public boolean isAbleToReceiveResult() {
        return isResumed();
    }

    @Override
    public void onResume() {
        super.onResume();
        TaskFragment taskFragment = getTaskFragment();
        if (taskFragment != null) {
            taskFragment.getResultIfItReturnedDuringPause();
        }
    }

}