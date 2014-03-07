package ru.droogcompanii.application.ui.util.able_to_start_task;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;

import ru.droogcompanii.application.ui.util.ActivityTrackedLifecycle;

/**
 * Created by ls on 06.03.14.
 */
public abstract class FragmentAbleToStartTask extends Fragment
        implements AbleToStartTask, TaskResultReceiver {

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

    public void startTask(int requestCode, TaskNotBeInterrupted task) {
        startTask(requestCode, task, TaskFragment.NO_TITLE_ID);
    }

    public void startTask(int requestCode, TaskNotBeInterrupted task, Integer title) {
        TaskFragment taskFragment = TaskFragment.newInstance(requestCode);
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

    public boolean isAbleToReceiveResult() {
        ActivityTrackedLifecycle activity = (ActivityTrackedLifecycle) getActivity();
        return (activity != null) && activity.isActivityResumed();
    }

    public abstract void onTaskResult(int requestCode, int resultCode, Serializable result);

    @Override
    public void onResume() {
        super.onResume();
        TaskFragment taskFragment = getTaskFragment();
        if (taskFragment != null) {
            taskFragment.getTaskResultIfItReturnedDuringDisactivity();
        }
    }

}
