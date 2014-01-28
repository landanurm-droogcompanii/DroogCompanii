package ru.droogcompanii.application.ui.fragment.filter_fragment.filters.helpers_for_more_filters;

import java.util.HashMap;
import java.util.Map;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.fragment.filter_fragment.filters.MoreFilters;

/**
 * Created by ls on 24.01.14.
 */
public class MoreFiltersHelperProvider {

    private static final String KEY_OF_DUMMY_HELPER = "";

    private static final Map<String, MoreFilters.Helper>
            helpersProvider = new HashMap<String, MoreFilters.Helper>();

    static {
        helpersProvider.put(KEY_OF_DUMMY_HELPER, new DummyHelper());
        // TODO:
    }

    public static MoreFilters.Helper get(PartnerCategory partnerCategory) {
        MoreFilters.Helper helper = helpersProvider.get(keyOf(partnerCategory));
        if (helper == null) {
            throw new RuntimeException("Not implemented yet!");
        }
        return helper;
    }

    private static String keyOf(PartnerCategory partnerCategory) {
        return (partnerCategory == null) ? KEY_OF_DUMMY_HELPER : partnerCategory.title;
    }
}
