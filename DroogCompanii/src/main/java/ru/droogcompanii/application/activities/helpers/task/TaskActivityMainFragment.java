package ru.droogcompanii.application.activities.helpers.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ls on 26.12.13.
 */
public abstract class TaskActivityMainFragment extends Fragment {

    public interface Callbacks {
        void onTaskFinished();
    }

    protected static final int TASK_FRAGMENT = 0;
    protected static final String TASK_FRAGMENT_TAG = "task";

    private static final Callbacks DUMMY_CALLBACKS = new Callbacks() {
        public void onTaskFinished() {
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

        TaskFragment taskFragment = (TaskFragment) fragmentManager.findFragmentByTag(TASK_FRAGMENT_TAG);
        if (taskFragment != null) {
            taskFragment.setTargetFragment(this, TASK_FRAGMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int fragmentLayoutId = getIdOfMainFragmentLayout();
        return inflater.inflate(fragmentLayoutId, container, false);
    }

    public void startTask() {
        TaskFragment taskFragment = prepareTaskFragment();
        taskFragment.setTask(prepareTask());
        taskFragment.setTargetFragment(this, TASK_FRAGMENT);
        showFragment(taskFragment);
    }

    protected abstract int getIdOfMainFragmentLayout();
    protected abstract TaskFragment prepareTaskFragment();
    protected abstract Task prepareTask();

    private void showFragment(TaskFragment taskFragment) {
        fragmentManager.beginTransaction().add(taskFragment, TASK_FRAGMENT_TAG).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == TASK_FRAGMENT) && (resultCode == Activity.RESULT_OK)) {
            callbacks.onTaskFinished();
        }
    }
}
