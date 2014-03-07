package ru.droogcompanii.application.ui.activity.able_to_start_task;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;

import ru.droogcompanii.application.ui.util.ActivityTrackedLifecycle;

/**
 * Created by ls on 06.03.14.
 */
public abstract class FragmentAbleToStartTask extends Fragment {

    protected static final int REQUEST_CODE_TASK_FRAGMENT = 2;
    protected static final String TAG_TASK_FRAGMENT = FragmentAbleToStartTask.class.getName() + "TAG_TASK_FRAGMENT";

    private FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getFragmentManager();

        TaskFragment taskFragment = getTaskFragment();
        if (taskFragment != null) {
            taskFragment.setTargetFragment(this, REQUEST_CODE_TASK_FRAGMENT);
        }
    }

    private TaskFragment getTaskFragment() {
        return (TaskFragment) fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);
    }

    protected void startTask(TaskNotBeInterrupted task) {
        startTask(task, TaskFragment.NO_TITLE_ID);
    }

    protected void startTask(TaskNotBeInterrupted task, Integer title) {
        TaskFragment taskFragment = new TaskFragment();
        taskFragment.setTitle(title);
        taskFragment.setTask(task);
        taskFragment.setTargetFragment(this, REQUEST_CODE_TASK_FRAGMENT);
        startFragment(taskFragment);
    }

    private void startFragment(TaskFragment taskFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(taskFragment, TAG_TASK_FRAGMENT);
        transaction.commit();
    }

    public final void onResult(int requestCode, int resultCode, Serializable result) {
        if ((requestCode == REQUEST_CODE_TASK_FRAGMENT)) {
            onResult(resultCode, result);
        }
    }

    public boolean isAbleToReceiveResult() {
        ActivityTrackedLifecycle activity = (ActivityTrackedLifecycle) getActivity();
        return (activity != null) && activity.isActivityStarted();
    }

    @Override
    public void onResume() {
        super.onResume();
        TaskFragment taskFragment = getTaskFragment();
        if (taskFragment != null) {
            taskFragment.getTaskResultIfItReturnedDuringDisactivity();
        }
    }

    protected abstract void onResult(int resultCode, Serializable result);

}
