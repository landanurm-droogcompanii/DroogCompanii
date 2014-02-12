package ru.droogcompanii.application.ui.activity.search_result_list;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResult;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResultImpl;
import ru.droogcompanii.application.ui.activity.search.SearchResultProvider;
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
        return (Serializable) toListOfSearchResults(searchResultProvider.getPartners(context));
    }

    private List<? extends SearchResult<Partner>> toListOfSearchResults(List<Partner> partners) {
        List<SearchResult<Partner>> searchResults = new ArrayList<SearchResult<Partner>>(partners.size());
        for (Partner partner : partners) {
            searchResults.add(new SearchResultImpl<Partner>(partner, true));
        }
        return searchResults;
    }
}
