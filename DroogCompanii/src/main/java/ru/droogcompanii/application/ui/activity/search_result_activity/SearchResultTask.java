package ru.droogcompanii.application.ui.activity.search_result_activity;

import android.content.Context;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableSortableListing;
import ru.droogcompanii.application.ui.activity.search_activity.SearchResultProvider;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterUtils;
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
        SearchableSortableListing<Partner> searchableSortableListing =
                SearchableSortableListing.newInstance(partners);
        addCurrentFiltersTo(searchableSortableListing);
        return (Serializable) searchableSortableListing.toListOfSearchResult();
    }

    private static void addCurrentFiltersTo(SearchableSortableListing<Partner> searchableSortableListing) {
        FilterSet filterSet = FilterUtils.getCurrentFilterSet(DroogCompaniiApplication.getContext());
        for (SearchCriterion<Partner> searchCriterion : filterSet.getPartnerSearchCriteria()) {
            searchableSortableListing.addSearchCriterion(searchCriterion);
        }
        for (Comparator<Partner> comparator : filterSet.getPartnerComparators()) {
            searchableSortableListing.addComparator(comparator);
        }
    }
}
