package ru.droogcompanii.application.ui.activity_3.search_activity.search_result_provider_impl;

import java.io.Serializable;

import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.util.MatcherToSearchQuery;

/**
 * Created by ls on 28.01.14.
 */
public class SearchCriterionByQuery implements SearchCriterion<String>, Serializable {

    private final MatcherToSearchQuery matcherToSearchQuery;

    public SearchCriterionByQuery(String query) {
        this.matcherToSearchQuery = new MatcherToSearchQuery(query);
    }

    @Override
    public boolean meetCriterion(String text) {
        return matcherToSearchQuery.matchToQuery(text);
    }
}
