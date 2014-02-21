package ru.droogcompanii.application.ui.activity.search_result_list;

import android.os.Bundle;

import java.io.Serializable;

import ru.droogcompanii.application.ui.activity.search.SearchResultProvider;
import ru.droogcompanii.application.ui.helpers.task.TaskNotBeInterrupted;
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
        searchResultProvider = getSearchResultProvider(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    private SearchResultProvider getSearchResultProvider(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return (SearchResultProvider) getArguments().getSerializable(Keys.searchResultProvider);
        } else {
            return (SearchResultProvider) savedInstanceState.getSerializable(Keys.searchResultProvider);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.searchResultProvider, (Serializable) searchResultProvider);
    }

    @Override
    protected Integer getTaskDialogTitleId() {
        return TaskFragment.NO_TITLE_ID;
    }

    @Override
    protected TaskNotBeInterrupted prepareTask() {
        return new SearchResultExtractorTask(searchResultProvider, getActivity());
    }
}
