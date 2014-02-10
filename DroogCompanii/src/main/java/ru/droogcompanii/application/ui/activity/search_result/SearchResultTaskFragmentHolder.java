package ru.droogcompanii.application.ui.activity.search_result;

import android.os.Bundle;

import java.io.Serializable;

import ru.droogcompanii.application.ui.activity.search.SearchResultProvider;
import ru.droogcompanii.application.ui.helpers.task.Task;
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
        searchResultProvider = getSearchResultProvider(savedInstanceState);
        if (savedInstanceState == null) {
            startTask();
        }
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
        return null;
    }

    @Override
    protected Task prepareTask() {
        return new SearchResultTask(searchResultProvider, getActivity());
    }
}
