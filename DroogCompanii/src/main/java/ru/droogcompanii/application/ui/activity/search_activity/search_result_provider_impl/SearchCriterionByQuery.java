package ru.droogcompanii.application.ui.activity.search_activity.search_result_provider_impl;

import java.io.Serializable;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.MatcherToSearchQuery;

/**
 * Created by ls on 28.01.14.
 */
class SearchCriterionByQuery implements Serializable {

    private final MatcherToSearchQuery matcherToSearchQuery;

    public SearchCriterionByQuery(String query) {
        this.matcherToSearchQuery = new MatcherToSearchQuery(query);
    }

    public boolean meetCriterion(String text) {
        return matcherToSearchQuery.matchToQuery(text);
    }

    public boolean meetCriterion(PartnerCategory category) {
        return matcherToSearchQuery.matchToQuery(category.title);
    }

    public boolean meetCriterion(Partner partner) {
        return matcherToSearchQuery.matchToQuery(partner.title);
    }

    public boolean meetCriterion(PartnerPoint partnerPoint) {
        return matcherToSearchQuery.matchToQuery(partnerPoint.title);
    }
}
