package ru.droogcompanii.application.ui.screens.synchronization;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.R;
import ru.droogcompanii.application.global_flags.VerifierDataForRelevance;
import ru.droogcompanii.application.util.ui.able_to_start_task.FragmentAbleToStartTask;
import ru.droogcompanii.application.util.ui.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;

/**
 * Created by ls on 03.04.14.
 */
public class SynchronizationFragment extends FragmentAbleToStartTask {

    public static interface Callbacks {
        void setResultAndFinish(int resultCode);
    }


    private static final int TASK_REQUEST_CODE_SYNCHRONIZATION = 244;

    private Callbacks callbacks;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.empty_frame_layout, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            startSynchronizationTask();
        }
    }

    private void startSynchronizationTask() {
        TaskNotBeInterruptedDuringConfigurationChange task = new SynchronizationTask(DroogCompaniiApplication.getContext());
        Integer title = R.string.titleOfSynchronizationDialog;
        startTask(TASK_REQUEST_CODE_SYNCHRONIZATION, task, title);
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
        if (requestCode == TASK_REQUEST_CODE_SYNCHRONIZATION) {
            onSynchronizationTaskFinished(resultCode, result);
        }
    }

    private void onSynchronizationTaskFinished(int resultCode, Serializable result) {
        if (resultCode == Activity.RESULT_OK) {
            boolean successful = (Boolean) result;
            if (!successful) {
                callbacks.setResultAndFinish(Activity.RESULT_CANCELED);
                return;
            }
            onSynchronizationTaskFinishedSuccessfully();
        }
        callbacks.setResultAndFinish(resultCode);
    }

    private void onSynchronizationTaskFinishedSuccessfully() {
        VerifierDataForRelevance.setDataIsUpdated();
    }
}