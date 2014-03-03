package ru.droogcompanii.application.ui.activity.search_result_list;

import android.content.Context;

import java.io.Serializable;

import ru.droogcompanii.application.ui.activity.search.SearchResultProvider;
import ru.droogcompanii.application.ui.activity.able_to_start_task.TaskNotBeInterrupted;

/**
 * Created by ls on 30.01.14.
 */
public class SearchResultsExtractorTask extends TaskNotBeInterrupted {
    private final SearchResultsExtractor searchResultsExtractor;

    public SearchResultsExtractorTask(SearchResultProvider searchResultProvider, Context context) {
        searchResultsExtractor = new SimpleSearchResultsExtractor(searchResultProvider, context);
    }

    @Override
    protected Serializable doInBackground(Void... voids) {
        return (Serializable) searchResultsExtractor.extract();
    }

}
