package ru.droogcompanii.application.ui.util.able_to_start_task;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 26.12.13.
 */
public class FragmentStartedTaskOnCreate extends FragmentAbleToStartTask {

    private static final int TASK_REQUEST_CODE = 156;

    public interface Callbacks {
        void onTaskFinished(int resultCode, Serializable result);
    }

    private static final Callbacks DUMMY_CALLBACKS = new Callbacks() {
        public void onTaskFinished(int resultCode, Serializable result) {
            // do nothing
        }
    };


    private Callbacks callbacks = DUMMY_CALLBACKS;
    private Integer titleId;
    private TaskNotBeInterruptedDuringConfigurationChange task;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement the TaskFragmentHolder's callbacks.");
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

        if (savedInstanceState == null) {
            startTask(TASK_REQUEST_CODE, task, titleId);
        }
    }

    public void set(TaskNotBeInterruptedDuringConfigurationChange task, Integer titleId) {
        this.task = task;
        this.titleId = titleId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.empty_frame_layout, container, false);
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
        callbacks.onTaskFinished(resultCode, result);
    }

}
