package ru.droogcompanii.application.util.ui.able_to_start_task;

import android.os.AsyncTask;

import java.io.Serializable;

/**
 * Created by ls on 26.12.13.
 */
public abstract class TaskNotBeInterruptedDuringConfigurationChange extends AsyncTask<Void, Void, Serializable> {

    private TaskFragment fragment;

    final void setFragment(TaskFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected final void onPostExecute(Serializable result) {
        if (fragment != null) {
            fragment.onTaskFinished(result);
        }
    }
}