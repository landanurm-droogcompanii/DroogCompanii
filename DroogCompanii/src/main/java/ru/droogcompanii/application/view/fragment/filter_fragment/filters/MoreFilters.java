package ru.droogcompanii.application.view.fragment.filter_fragment.filters;

import android.content.Context;
import android.view.View;

import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.view.fragment.filter_fragment.Filter;
import ru.droogcompanii.application.view.fragment.filter_fragment.filters.helpers_for_more_filters.MoreFiltersHelperProvider;

/**
 * Created by ls on 24.01.14.
 */
public class MoreFilters extends BaseFilters {

    public static interface Helper {
        List<Filter> prepareDefaultFilters();
        View prepareViewOfFilters(Context context);
    }

    private final Helper helper;
    private final List<Filter> filters;

    public MoreFilters(PartnerCategory partnerCategory) {
        helper = MoreFiltersHelperProvider.get(partnerCategory);
        filters = helper.prepareDefaultFilters();
    }

    @Override
    protected List<Filter> getFilters() {
        return filters;
    }

    @Override
    protected View prepareViewOfFilters(Context context) {
        return helper.prepareViewOfFilters(context);
    }


}
