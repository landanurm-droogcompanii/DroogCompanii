package ru.droogcompanii.application.ui.activity.search_result_list;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResult;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResultImpl;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableSortableListing;
import ru.droogcompanii.application.ui.activity.search.SearchResultProvider;
import ru.droogcompanii.application.ui.fragment.filter.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter.FilterUtils;

/**
 * Created by ls on 17.02.14.
 */
public class SearchResultsExtractorUsedCurrentFilters implements SearchResultsExtractor {
    private final Context context;
    private final SearchResultProvider searchResultProvider;

    public SearchResultsExtractorUsedCurrentFilters(SearchResultProvider searchResultProvider, Context context) {
        this.searchResultProvider = searchResultProvider;
        this.context = context;
    }

    @Override
    public List<SearchResult<Partner>> extract() {
        List<Partner> partners = searchResultProvider.getPartners(context);
        List<SearchResult<Partner>> resultsOfSearchUsingPartnerPoints = searchUsingPartnerPoints(partners);
        return filter(partners, resultsOfSearchUsingPartnerPoints);
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
        FilterSet filterSet = FilterUtils.getCurrentFilterSet(DroogCompaniiApplication.getContext());
        searchableListing.addSearchCriterion(filterSet.getCombinedPartnerPointSearchCriterion());
        return searchableListing.toList().size() > 0;
    }

    private static List<SearchResult<Partner>> filter(List<Partner> partners,
                                                      List<SearchResult<Partner>> searchResults) {
        SearchableSortableListing<Partner> searchableSortableListing =
                SearchableSortableListing.newInstance(partners);
        addCurrentFiltersTo(searchableSortableListing);
        List<SearchResult<Partner>> filtered = searchableSortableListing.toListOfSearchResults();
        return combine(filtered, searchResults);
    }

    private static void addCurrentFiltersTo(SearchableSortableListing<Partner> searchableSortableListing) {
        FilterSet filterSet = FilterUtils.getCurrentFilterSet(DroogCompaniiApplication.getContext());
        searchableSortableListing.addSearchCriterion(filterSet.getCombinedPartnerSearchCriterion());
        searchableSortableListing.addComparator(CurrentComparatorProvider.getCombinedPartnerComparator());
    }

    private static List<SearchResult<Partner>> combine(List<SearchResult<Partner>> filtered,
                                                       List<SearchResult<Partner>> searchResults) {
        for (SearchResult<Partner> searchResult : searchResults) {
            if (!searchResult.meetsSearchCriteria()) {
                markThatPartnerDoesNotMeetSearchCriteria(filtered, searchResult.value());
            }
        }
        return filtered;
    }

    private static void markThatPartnerDoesNotMeetSearchCriteria(
            List<SearchResult<Partner>> filtered, Partner partner) {
        final int size = filtered.size();
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
