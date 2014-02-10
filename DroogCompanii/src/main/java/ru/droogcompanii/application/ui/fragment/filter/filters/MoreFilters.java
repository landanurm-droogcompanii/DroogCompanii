package ru.droogcompanii.application.ui.fragment.filter.filters;

import android.content.Context;
import android.view.View;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.fragment.filter.Filter;
import ru.droogcompanii.application.ui.fragment.filter.filters.helpers_for_more_filters.MoreFiltersHelperProvider;

/**
 * Created by ls on 24.01.14.
 */
public class MoreFilters extends BaseFilters implements Serializable {

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        MoreFilters other = (MoreFilters) obj;
        return filters.equals(other.filters) && helper.equals(other.helper);
    }

    @Override
    public int hashCode() {
        int result = helper != null ? helper.hashCode() : 0;
        result = 31 * result + (filters != null ? filters.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Filter filter : filters) {
            result.append(filter.toString() + "\n");
        }
        return result.toString();
    }


}
