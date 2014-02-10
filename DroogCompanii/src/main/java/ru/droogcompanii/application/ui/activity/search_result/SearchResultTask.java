package ru.droogcompanii.application.ui.activity.search_result;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResult;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResultImpl;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableSortableListing;
import ru.droogcompanii.application.ui.activity.search.SearchResultProvider;
import ru.droogcompanii.application.ui.fragment.filter.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter.FilterUtils;
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
        List<SearchResult<Partner>> resultsOfSearchUsingPartnerPoints = searchUsingPartnerPoints(partners);
        return (Serializable) filter(partners, resultsOfSearchUsingPartnerPoints);
    }

    private List<SearchResult<Partner>> searchUsingPartnerPoints(List<Partner> partners) {
        List<SearchResult<Partner>> searchResults = new ArrayList<SearchResult<Partner>>(partners.size());
        for (Partner partner : partners) {
            searchResults.add(searchResultFrom(partner));
        }
        return searchResults;
    }

    private SearchResult<Partner> searchResultFrom(Partner partner) {
        return new SearchResultImpl<Partner>(partner, definePartnerMeetsCriterion(partner));
    }

    private boolean definePartnerMeetsCriterion(Partner partner) {
        List<PartnerPoint> partnerPoints = searchResultProvider.getPointsOfPartner(context, partner);
        SearchableListing<PartnerPoint> searchableListing = SearchableListing.newInstance(partnerPoints);
        addCurrentSearchFiltersTo(searchableListing);
        return searchableListing.toList().size() > 0;
    }

    private static void addCurrentSearchFiltersTo(SearchableListing<PartnerPoint> searchableListing) {
        FilterSet filterSet = FilterUtils.getCurrentFilterSet(DroogCompaniiApplication.getContext());
        for (SearchCriterion<PartnerPoint> searchCriterion : filterSet.getPartnerPointSearchCriteria()) {
            searchableListing.addSearchCriterion(searchCriterion);
        }
    }

    private static List<? extends SearchResult<Partner>> filter(List<Partner> partners,
                                               List<SearchResult<Partner>> searchResults) {
        SearchableSortableListing<Partner> searchableSortableListing =
                SearchableSortableListing.newInstance(partners);
        addCurrentFiltersTo(searchableSortableListing);
        List<SearchResult<Partner>> filtered = searchableSortableListing.toListOfSearchResults();
        return combine(filtered, searchResults);
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

    private static List<? extends SearchResult<Partner>> combine(
                                    List<SearchResult<Partner>> filtered,
                                    List<SearchResult<Partner>> searchResults) {
        for (SearchResult<Partner> searchResult : searchResults) {
            if (!searchResult.meetsSearchCriteria()) {
                setPartnerDoesNotMeetSearchCriteria(filtered, searchResult.value());
            }
        }
        return filtered;
    }

    private static void setPartnerDoesNotMeetSearchCriteria(
            List<SearchResult<Partner>> filtered, Partner partner) {

        int size = filtered.size();
        for (int i = 0; i < size; ++i) {
            SearchResult<Partner> searchResult = filtered.get(i);
            if (partner.equals(searchResult.value())) {
                SearchResult<Partner> changed = new SearchResultImpl<Partner>(partner, false);
                filtered.set(i, changed);
                return;
            }
        }
    }
}
