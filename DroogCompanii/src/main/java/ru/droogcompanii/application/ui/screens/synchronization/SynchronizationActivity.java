package ru.droogcompanii.application.ui.screens.synchronization;

import android.os.Bundle;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.global_flags.VerifierDataForRelevance;
import ru.droogcompanii.application.util.ui.able_to_start_task.ActivityAbleToStartTask;
import ru.droogcompanii.application.util.ui.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;

/**
 * Created by ls on 26.12.13.
 */
public class SynchronizationActivity extends ActivityAbleToStartTask {

    public static final int REQUEST_CODE = 157;

    private static final int TASK_REQUEST_CODE_SYNCHRONIZATION = 244;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronization);

        if (savedInstanceState == null) {
            startSynchronizationTask();
        }
    }

    private void startSynchronizationTask() {
        TaskNotBeInterruptedDuringConfigurationChange task = new SynchronizationTask(this);
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
        if (resultCode == RESULT_OK) {
            onSynchronizationTaskFinishedSuccessfully();
        }
        setResult(resultCode);
        finish();
    }

    private void onSynchronizationTaskFinishedSuccessfully() {
        VerifierDataForRelevance.setDataIsUpdated();
    }
}
