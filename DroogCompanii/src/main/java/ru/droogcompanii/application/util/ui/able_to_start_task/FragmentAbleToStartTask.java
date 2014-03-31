package ru.droogcompanii.application.util.ui.able_to_start_task;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by ls on 06.03.14.
 */
public abstract class FragmentAbleToStartTask extends Fragment
        implements AbleToStartTask, TaskResultReceiver {

    protected static final String TAG_TASK_FRAGMENT = FragmentAbleToStartTask.class.getName() + "TAG_TASK_FRAGMENT";

    private static final String KEY_REQUEST_CODES = "KEY_REQUEST_CODES";

    private Collection<Integer> requestCodes;
    private FragmentManager fragmentManager;


    public FragmentAbleToStartTask() {
        requestCodes = new HashSet<Integer>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }

        fragmentManager = getFragmentManager();

        setTargetForTaskFragments();
    }

    private void setTargetForTaskFragments() {
        for (Integer eachRequestCode : requestCodes) {
            TaskFragment taskFragment = getTaskFragment(eachRequestCode);
            if (taskFragment != null) {
                taskFragment.setTargetFragment(this, eachRequestCode);
            }
        }
    }

    private TaskFragment getTaskFragment(int requestCode) {
        String tag = tagByRequestCode(requestCode);
        return (TaskFragment) fragmentManager.findFragmentByTag(tag);
    }

    private void restoreState(Bundle savedInstanceState) {
        requestCodes = (Collection<Integer>) savedInstanceState.getSerializable(KEY_REQUEST_CODES);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_REQUEST_CODES, (Serializable) requestCodes);
    }

    public void startTask(int requestCode, TaskNotBeInterruptedDuringConfigurationChange task) {
        startTask(requestCode, task, TaskFragment.NO_TITLE_ID);
    }

    public void startTask(int requestCode, TaskNotBeInterruptedDuringConfigurationChange task, Integer title) {
        requestCodes.add(requestCode);
        TaskFragment taskFragment = TaskFragment.newInstance(requestCode);
        taskFragment.setTitle(title);
        taskFragment.setTask(task);
        taskFragment.setTargetFragment(this, requestCode);
        startFragment(requestCode, taskFragment);
    }

    private void startFragment(int requestCode, TaskFragment taskFragment) {
        String tag = tagByRequestCode(requestCode);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        TaskFragment previousTaskFragment = findTaskFragmentByTag(tag);
        if (previousTaskFragment != null) {
            previousTaskFragment.detachTask();
            transaction.remove(previousTaskFragment);
        }
        transaction.add(taskFragment, tag);
        transaction.commit();
    }

    private TaskFragment findTaskFragmentByRequestCode(int requestCode) {
        String tag = tagByRequestCode(requestCode);
        return findTaskFragmentByTag(tag);
    }

    private TaskFragment findTaskFragmentByTag(String tag) {
        return (TaskFragment) fragmentManager.findFragmentByTag(tag);
    }

    private static String tagByRequestCode(int requestCode) {
        return TAG_TASK_FRAGMENT + " " + requestCode;
    }

    public void cancelTask(int requestCode) {
        TaskFragment taskFragment = findTaskFragmentByRequestCode(requestCode);
        if (taskFragment != null) {
            taskFragment.detachTask();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(taskFragment);
            transaction.commit();
        }
    }

    public boolean isTaskStarted(int requestCode) {
        return requestCodes.contains(requestCode);
    }

    public void onTaskFinished(int requestCode, int resultCode, Serializable result) {
        requestCodes.remove(requestCode);
        onTaskResult(requestCode, resultCode, result);
    }

    public abstract void onTaskResult(int requestCode, int resultCode, Serializable result);

    public boolean isAbleToReceiveResult() {
        return isResumed();
    }

    @Override
    public void onResume() {
        super.onResume();
        getResultsWhichReturnedDuringDisabilityToReceiveIt();
    }

    private void getResultsWhichReturnedDuringDisabilityToReceiveIt() {
        for (int eachRequestCode : requestCodes) {
            TaskFragment taskFragment = getTaskFragment(eachRequestCode);
            if (taskFragment != null) {
                taskFragment.getResultIfItReturnedWhenResultReceiverDisabledToReceiveIt();
            }
        }
    }

}