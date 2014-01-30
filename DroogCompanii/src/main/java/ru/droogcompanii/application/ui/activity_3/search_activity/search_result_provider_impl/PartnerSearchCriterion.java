package ru.droogcompanii.application.ui.activity_3.search_activity.search_result_provider_impl;

import java.util.List;

import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerPointsReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;

/**
 * Created by ls on 29.01.14.
 */
public class PartnerSearchCriterion implements SearchCriterion<Partner> {
    private final SearchCriterionByQuery searchCriterion;
    private final PartnerPointsReader partnerPointsReader;
    private final PartnerPointSearchCriterion partnerPointsSearchCriterion;

    public PartnerSearchCriterion(SearchCriterionByQuery searchCriterion, PartnerPointsReader partnerPointsReader) {
        this.searchCriterion = searchCriterion;
        this.partnerPointsReader = partnerPointsReader;
        this.partnerPointsSearchCriterion = new PartnerPointSearchCriterion(searchCriterion);
    }

    @Override
    public boolean meetCriterion(Partner partner) {
        return searchCriterion.meetCriterion(partner.title) ||
                partnerPointsOfPartnerMeetCriterion(partner);
    }

    private boolean partnerPointsOfPartnerMeetCriterion(Partner partner) {
        List<PartnerPoint> partnerPoints = partnerPointsReader.getPartnerPointsOf(partner);
        for (PartnerPoint partnerPoint : partnerPoints) {
            if (partnerPointsSearchCriterion.meetCriterion(partnerPoint)) {
                return true;
            }
        }
        return false;
    }
}
