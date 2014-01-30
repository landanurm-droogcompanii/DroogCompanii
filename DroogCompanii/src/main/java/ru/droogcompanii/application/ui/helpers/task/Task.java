package ru.droogcompanii.application.ui.helpers.task;

import android.os.AsyncTask;

import java.io.Serializable;

/**
 * Created by ls on 26.12.13.
 */
public abstract class Task extends AsyncTask<Void, Void, Serializable> {

    private TaskFragment fragment;

    void setFragment(TaskFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected void onPostExecute(Serializable result) {
        if (fragment != null) {
            fragment.onTaskFinished(result);
        }
    }
}
