package ru.droogcompanii.application.ui.helpers.task;

import android.os.AsyncTask;

/**
 * Created by ls on 26.12.13.
 */
public abstract class Task extends AsyncTask<Void, Void, Void> {

    private TaskFragment fragment;

    void setFragment(TaskFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected void onPostExecute(Void unused) {
        if (fragment != null) {
            fragment.onTaskFinished();
        }
    }
}
