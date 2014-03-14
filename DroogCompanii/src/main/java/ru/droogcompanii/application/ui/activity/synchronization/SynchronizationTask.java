package ru.droogcompanii.application.ui.activity.synchronization;

import android.content.Context;

import java.io.Serializable;

import ru.droogcompanii.application.ui.util.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;
import ru.droogcompanii.application.util.Snorlax;

/**
 * Created by ls on 26.12.13.
 */
public class SynchronizationTask extends TaskNotBeInterruptedDuringConfigurationChange {
    private final SynchronizationWorker worker;

    public SynchronizationTask(Context context) {
        worker = new SynchronizationWorker(context);
    }

    @Override
    protected Serializable doInBackground(Void... params) {
        worker.execute();
        Snorlax.sleep();
        return null;
    }
}
