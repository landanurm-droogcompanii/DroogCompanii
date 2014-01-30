package ru.droogcompanii.application.ui.activity_3.search_result_activity;

import android.os.Bundle;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.activity_3.search_activity.SearchResultProvider;
import ru.droogcompanii.application.ui.helpers.task.Task;
import ru.droogcompanii.application.ui.helpers.task.TaskFragment;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 30.01.14.
 */
public class SearchResultTaskFragmentHolder extends TaskFragmentHolder {

    private SearchResultProvider searchResultProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        searchResultProvider = (SearchResultProvider) args.getSerializable(Keys.searchResultProvider);
        if (isActivityFirstLaunched()) {
            startTask();
        }
    }

    private boolean isActivityFirstLaunched() {
        SearchResultActivity activity = (SearchResultActivity) getActivity();
        return activity.isFirstLaunched();
    }

    @Override
    protected int getLayoutIdOfTaskFragmentHolder() {
        return R.layout.empty_layout;
    }

    @Override
    protected TaskFragment prepareTaskFragment() {
        return new SearchResultTaskFragment();
    }

    @Override
    protected Task prepareTask() {
        return new SearchResultTask(searchResultProvider, getActivity());
    }
}
