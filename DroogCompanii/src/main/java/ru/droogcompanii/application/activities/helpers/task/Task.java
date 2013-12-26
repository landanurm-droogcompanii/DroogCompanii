package ru.droogcompanii.application.activities.helpers.task;

import android.os.AsyncTask;

/**
 * Created by ls on 26.12.13.
 */
public class Task extends AsyncTask<Void, Void, Void> {

    private TaskFragment fragment;

    void setFragment(TaskFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected Void doInBackground(Void... params) {
        // Child classes must override this method
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        if (fragment != null) {
            fragment.onTaskFinished();
        }
    }
}
