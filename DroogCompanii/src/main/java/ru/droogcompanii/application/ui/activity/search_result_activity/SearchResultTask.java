package ru.droogcompanii.application.ui.activity.search_result_activity;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.ui.activity.search_activity.SearchResultProvider;
import ru.droogcompanii.application.ui.helpers.task.Task;

/**
 * Created by ls on 30.01.14.
 */
public class SearchResultTask extends Task {
    private final Context context;
    private final SearchResultProvider searchResultProvider;

    public SearchResultTask(SearchResultProvider searchResultProvider, Context context) {
        this.searchResultProvider = searchResultProvider;
        this.context = context;
    }

    @Override
    protected Serializable doInBackground(Void... voids) {
        List<Partner> partners = searchResultProvider.getPartners(context);
        return (Serializable) partners;
    }
}
