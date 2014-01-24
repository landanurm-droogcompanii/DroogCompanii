package ru.droogcompanii.application.view.fragment.filter_fragment.filters;

import android.view.View;

import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.view.fragment.filter_fragment.Filter;

/**
 * Created by ls on 24.01.14.
 */
public class MoreFilters extends BaseFilters {

    public static interface Helper {
        List<Filter> prepareDefaultFilters();
        View prepareViewOfFilters(View containerView);
    }

    private final Helper helper;
    private final List<Filter> filters;

    public MoreFilters(PartnerCategory partnerCategory) {
        helper = MoreFiltersHelperBuilder.build(partnerCategory);
        filters = helper.prepareDefaultFilters();
    }

    @Override
    protected List<Filter> getFilters() {
        return filters;
    }

    @Override
    protected View prepareViewOfFilters(View containerView) {
        return helper.prepareViewOfFilters(containerView);
    }


}
