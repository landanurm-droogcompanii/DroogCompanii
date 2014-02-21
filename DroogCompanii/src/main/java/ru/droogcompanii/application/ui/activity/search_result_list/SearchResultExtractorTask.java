package ru.droogcompanii.application.ui.activity.search_result_list;

import android.content.Context;

import java.io.Serializable;

import ru.droogcompanii.application.ui.activity.search.SearchResultProvider;
import ru.droogcompanii.application.ui.helpers.task.TaskNotBeInterrupted;

/**
 * Created by ls on 30.01.14.
 */
public class SearchResultExtractorTask extends TaskNotBeInterrupted {
    private final SearchResultExtractor searchResultExtractor;

    public SearchResultExtractorTask(SearchResultProvider searchResultProvider, Context context) {
        searchResultExtractor = new SimpleSearchResultExtractor(searchResultProvider, context);
    }

    @Override
    protected Serializable doInBackground(Void... voids) {
        return (Serializable) searchResultExtractor.extract();
    }

}
