package ru.droogcompanii.application.ui.helpers.task;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

/**
 * Created by ls on 26.12.13.
 */
public abstract class TaskFragmentHolder extends Fragment {

    public interface Callbacks {
        void onTaskFinished(int resultCode, Serializable result);
    }

    protected static final int REQUEST_CODE_TASK_FRAGMENT = 0;
    protected static final String TAG_TASK_FRAGMENT = "task";

    private static final Callbacks DUMMY_CALLBACKS = new Callbacks() {
        public void onTaskFinished(int resultCode, Serializable result) {
            // do nothing
        }
    };

    private FragmentManager fragmentManager;
    private Callbacks callbacks = DUMMY_CALLBACKS;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = DUMMY_CALLBACKS;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getFragmentManager();

        TaskFragment taskFragment = (TaskFragment) fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);
        if (taskFragment != null) {
            taskFragment.setTargetFragment(this, REQUEST_CODE_TASK_FRAGMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int fragmentLayoutId = getLayoutIdOfTaskFragmentHolder();
        return inflater.inflate(fragmentLayoutId, container, false);
    }

    protected abstract int getLayoutIdOfTaskFragmentHolder();

    protected void startTask() {
        TaskFragment taskFragment = prepareTaskFragment();
        taskFragment.setTask(prepareTask());
        taskFragment.setTargetFragment(this, REQUEST_CODE_TASK_FRAGMENT);
        startFragment(taskFragment);
    }

    protected abstract TaskFragment prepareTaskFragment();
    protected abstract Task prepareTask();

    private void startFragment(TaskFragment taskFragment) {
        fragmentManager.beginTransaction().add(taskFragment, TAG_TASK_FRAGMENT).commit();
    }

    public void onResult(int requestCode, int resultCode, Serializable result) {
        if ((requestCode == REQUEST_CODE_TASK_FRAGMENT)) {
            callbacks.onTaskFinished(resultCode, result);
        }
    }
}
