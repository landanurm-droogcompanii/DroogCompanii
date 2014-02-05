package ru.droogcompanii.application.ui.fragment.filter_fragment.filters.helpers_for_more_filters;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.fragment.filter_fragment.filters.MoreFilters;

/**
 * Created by ls on 24.01.14.
 */
public class MoreFiltersHelperProvider {

    public static MoreFilters.Helper get(PartnerCategory partnerCategory) {
        // TODO:
        // different filters for different categories
        return new DummyHelper();
    }
}
