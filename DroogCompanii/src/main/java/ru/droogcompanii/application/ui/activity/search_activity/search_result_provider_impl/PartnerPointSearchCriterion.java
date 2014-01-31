package ru.droogcompanii.application.ui.activity.search_activity.search_result_provider_impl;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;

/**
 * Created by ls on 29.01.14.
 */
class PartnerPointSearchCriterion implements SearchCriterion<PartnerPoint> {
    private final SearchCriterionByQuery searchCriterion;

    public PartnerPointSearchCriterion(SearchCriterionByQuery searchCriterion) {
        this.searchCriterion = searchCriterion;
    }

    @Override
    public boolean meetCriterion(PartnerPoint partnerPoint) {
        return searchCriterion.meetCriterion(partnerPoint.title);
    }
}