package ru.droogcompanii.application.ui.activity.synchronization;

import android.content.Context;

import java.io.Serializable;

import ru.droogcompanii.application.ui.helpers.task.Task;
import ru.droogcompanii.application.util.Snorlax;

/**
 * Created by ls on 26.12.13.
 */
public class SynchronizationTask extends Task {
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
