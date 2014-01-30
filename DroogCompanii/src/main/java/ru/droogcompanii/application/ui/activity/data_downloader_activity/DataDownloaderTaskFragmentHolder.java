package ru.droogcompanii.application.ui.activity.data_downloader_activity;

import android.os.Bundle;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.helpers.task.Task;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;
import ru.droogcompanii.application.ui.helpers.task.TaskFragment;

/**
 * Created by ls on 26.12.13.
 */
public class DataDownloaderTaskFragmentHolder extends TaskFragmentHolder {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isActivityFirstLaunched()) {
            startTask();
        }
    }

    private boolean isActivityFirstLaunched() {
        DataDownloaderActivity activity = (DataDownloaderActivity) getActivity();
        return !activity.screenRotated();
    }

    @Override
    protected int getIdOfMainFragmentLayout() {
        return R.layout.fragment_data_downloader;
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
