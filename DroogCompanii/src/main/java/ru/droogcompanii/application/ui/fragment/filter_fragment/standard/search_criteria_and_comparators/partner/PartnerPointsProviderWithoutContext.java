package ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators.partner;

import android.content.Context;

import java.util.List;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerCategoriesReader;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerPointsReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterUtils;

/**
 * Created by ls on 05.02.14.
 */
class PartnerPointsProviderWithoutContext {
    static List<PartnerPoint> getPointsOf(Partner partner) {
        PartnerCategoriesReader partnerCategoriesReader = new PartnerCategoriesReader(getApplicationContext());
        PartnerCategory partnerCategory = partnerCategoriesReader.getPartnerCategoryOf(partner);
        PartnerPointsReader partnerPointsReader = new PartnerPointsReader(getApplicationContext());
        return applySearchFilters(partnerCategory, partnerPointsReader.getPartnerPointsOf(partner));
    }

    private static Context getApplicationContext() {
        return DroogCompaniiApplication.getContext();
    }

    private static List<PartnerPoint> applySearchFilters(
            PartnerCategory partnerCategory, List<PartnerPoint> partnerPoints) {
        SearchableListing<PartnerPoint> searchableListing = SearchableListing.newInstance(partnerPoints);
        addCurrentSearchCriteria(partnerCategory, searchableListing);
        return searchableListing.toList();
    }

    private static void addCurrentSearchCriteria(
            PartnerCategory partnerCategory, SearchableListing<PartnerPoint> searchableListing) {
        FilterSet filterSet = FilterUtils.getCurrentFilterSet(getApplicationContext(), partnerCategory);
        for (SearchCriterion<PartnerPoint> criterion : filterSet.getPartnerPointSearchCriteria()) {
            searchableListing.addSearchCriterion(criterion);
        }
    }
}
