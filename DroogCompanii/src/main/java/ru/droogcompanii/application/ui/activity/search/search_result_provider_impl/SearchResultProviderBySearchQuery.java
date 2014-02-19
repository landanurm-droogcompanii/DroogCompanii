package ru.droogcompanii.application.ui.activity.search.search_result_provider_impl;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerCategoriesReader;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerPointsReader;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;
import ru.droogcompanii.application.ui.activity.search.SearchResultProvider;
import ru.droogcompanii.application.ui.fragment.filter.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter.FilterUtils;

/**
 * Created by ls on 28.01.14.
 */
public class SearchResultProviderBySearchQuery implements SearchResultProvider, Serializable {

    private final String searchQuery;
    private final SearchCriterionByQuery searchCriterion;

    public SearchResultProviderBySearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
        this.searchCriterion = new SearchCriterionByQuery(searchQuery);
    }

    @Override
    public String getTitle(Context context) {
        return searchQuery;
    }

    @Override
    public List<Partner> getPartners(Context context) {
        PartnerCategoriesReader partnerCategoriesReader = new PartnerCategoriesReader(context);
        Set<Partner> partners = new HashSet<Partner>();
        for (PartnerCategory category : partnerCategoriesReader.getPartnerCategories()) {
            partners.addAll(getPartnersOfCategoryWhichMeetCriterion(context, category));
        }
        return new ArrayList<Partner>(partners);
    }

    private Collection<Partner> getPartnersOfCategoryWhichMeetCriterion(Context context, PartnerCategory category) {
        PartnersReader partnersReader = new PartnersReader(context);
        if (searchCriterion.meetCriterion(category)) {
            return partnersReader.getPartnersOf(category);
        }
        Collection<Partner> partnersWhichMeetCriterion = new HashSet<Partner>();
        for (Partner partner : partnersReader.getPartnersOf(category)) {
            if (searchCriterion.meetCriterion(partner) || somePointsOfPartnerMeetCriterion(context, partner)) {
                partnersWhichMeetCriterion.add(partner);
            }
        }
        return partnersWhichMeetCriterion;
    }

    private boolean somePointsOfPartnerMeetCriterion(Context context, Partner partner) {
        for (PartnerPoint partnerPoint : readAllPointsOfPartner(context, partner)) {
            if (searchCriterion.meetCriterion(partnerPoint)) {
                return true;
            }
        }
        return false;
    }

    private static List<PartnerPoint> readAllPointsOfPartner(Context context, Partner partner) {
        PartnerPointsReader reader = new PartnerPointsReader(context);
        return reader.getPartnerPointsOf(partner);
    }

    @Override
    public List<PartnerPoint> getPointsOfPartner(Context context, Partner partner) {
        if (searchCriterion.meetCriterion(partner)) {
            return readAllPointsOfPartner(context, partner);
        }
        PartnerCategoriesReader partnerCategoriesReader = new PartnerCategoriesReader(context);
        PartnerCategory partnerCategory = partnerCategoriesReader.getPartnerCategoryOf(partner);
        if (searchCriterion.meetCriterion(partnerCategory)) {
            return readAllPointsOfPartner(context, partner);
        }
        return getFilteredPointsOfPartner(context, partner);
    }

    private List<PartnerPoint> getFilteredPointsOfPartner(Context context, Partner partner) {
        return filtered(readAllPointsOfPartner(context, partner));
    }

    private List<PartnerPoint> filtered(List<PartnerPoint> partnerPoints) {
        Collection<PartnerPoint> filtered = new HashSet<PartnerPoint>();
        for (PartnerPoint partnerPoint : partnerPoints) {
            if (searchCriterion.meetCriterion(partnerPoint)) {
                filtered.add(partnerPoint);
            }
        }
        return new ArrayList<PartnerPoint>(filtered);
    }

    @Override
    public List<PartnerPoint> getAllPartnerPoints(Context context) {
        Collection<PartnerPoint> partnerPoints = new HashSet<PartnerPoint>();
        for (Partner partner : getPartners(context)) {
            partnerPoints.addAll(getPointsOfPartner(context, partner));
        }
        return applyCurrentSearchFiltersTo(context, partnerPoints);
    }

    private List<PartnerPoint> applyCurrentSearchFiltersTo(Context context,
                                                           Collection<PartnerPoint> partnerPoints) {
        FilterSet currentFilterSet = FilterUtils.getCurrentFilterSet(context);
        SearchableListing<PartnerPoint> searchableListing = SearchableListing.newInstance(partnerPoints);
        searchableListing.addSearchCriterion(currentFilterSet.getCombinedPartnerPointSearchCriterion());
        return searchableListing.toList();
    }
}
