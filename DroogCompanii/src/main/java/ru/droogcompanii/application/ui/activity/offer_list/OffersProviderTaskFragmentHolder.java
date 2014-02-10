package ru.droogcompanii.application.ui.activity.offer_list;

import android.content.Context;
import android.os.Bundle;

import ru.droogcompanii.application.ui.helpers.task.Task;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;

/**
 * Created by ls on 10.02.14.
 */
public class OffersProviderTaskFragmentHolder extends TaskFragmentHolder {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            startTask();
        }
    }

    @Override
    protected Integer getTaskDialogTitleId() {
        return null;
    }

    @Override
    protected Task prepareTask() {
        Context context = getActivity();
        return new OffersProviderTask(context);
    }
}
