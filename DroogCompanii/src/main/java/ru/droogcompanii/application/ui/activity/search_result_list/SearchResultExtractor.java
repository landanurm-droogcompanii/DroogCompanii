package ru.droogcompanii.application.ui.activity.search_result_list;

import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResult;

/**
 * Created by ls on 17.02.14.
 */
public interface SearchResultExtractor {
    List<SearchResult<Partner>> extract();
}
