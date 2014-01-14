package ru.droogcompanii.application.view.activity.data_downloader_activity;

import android.os.Bundle;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.view.helpers.task.Task;
import ru.droogcompanii.application.view.helpers.task.TaskActivityMainFragment;
import ru.droogcompanii.application.view.helpers.task.TaskFragment;

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
