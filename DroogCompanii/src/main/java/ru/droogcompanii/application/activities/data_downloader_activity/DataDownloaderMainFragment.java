package ru.droogcompanii.application.activities.data_downloader_activity;

import android.os.Bundle;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.activities.helpers.task.Task;
import ru.droogcompanii.application.activities.helpers.task.TaskActivityMainFragment;
import ru.droogcompanii.application.activities.helpers.task.TaskFragment;

/**
 * Created by ls on 26.12.13.
 */
public class DataDownloaderMainFragment extends TaskActivityMainFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            startTask();
        }
    }

    @Override
    protected int getMainFragmentLayoutIdOfTaskActivity() {
        return R.layout.data_downloader_fragment;
    }

    @Override
    protected TaskFragment prepareTaskFragment() {
        return new DataDownloaderTaskFragment();
    }

    @Override
    protected Task prepareTask() {
        return new DataDownloaderTask(getActivity());
    }
}
