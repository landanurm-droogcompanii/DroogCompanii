package ru.droogcompanii.application.activity.data_downloader_activity;

import android.os.Bundle;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.activity.helpers.task.Task;
import ru.droogcompanii.application.activity.helpers.task.TaskActivityMainFragment;
import ru.droogcompanii.application.activity.helpers.task.TaskFragment;

/**
 * Created by ls on 26.12.13.
 */
public class DataDownloaderMainFragment extends TaskActivityMainFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (activityFirstLaunched()) {
            startTask();
        }
    }

    private boolean activityFirstLaunched() {
        DataDownloaderActivity activity = (DataDownloaderActivity) getActivity();
        return !activity.screenRotated();
    }

    @Override
    protected int getIdOfMainFragmentLayout() {
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
