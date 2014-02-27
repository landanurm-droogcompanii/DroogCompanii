package ru.droogcompanii.application.ui.activity.search_result_list;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResult;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResultImpl;
import ru.droogcompanii.application.ui.activity.search.SearchResultProvider;

/**
 * Created by ls on 17.02.14.
 */
public class SimpleSearchResultsExtractor implements SearchResultsExtractor {
    private final Context context;
    private final SearchResultProvider searchResultProvider;

    public SimpleSearchResultsExtractor(SearchResultProvider searchResultProvider, Context context) {
        this.searchResultProvider = searchResultProvider;
        this.context = context;
    }

    @Override
    public List<SearchResult<Partner>> extract() {
        List<Partner> partners = searchResultProvider.getPartners(context);
        List<SearchResult<Partner>> searchResults = new ArrayList<SearchResult<Partner>>(partners.size());
        for (Partner partner : partners) {
            searchResults.add(new SearchResultImpl<Partner>(partner, true));
        }
        return searchResults;
    }

}
