package ru.droogcompanii.application.ui.fragment.filter.filters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.fragment.filter.Filter;
import ru.droogcompanii.application.ui.fragment.filter.standard.filter_impl.StandardFiltersProvider;

/**
 * Created by ls on 24.01.14.
 */
public class StandardFilters extends BaseFilters implements Serializable {
    private final List<Filter> filters;

    public StandardFilters() {
        filters = StandardFiltersProvider.getDefaultFilters();
    }

    @Override
    protected List<Filter> getFilters() {
        return filters;
    }

    @Override
    protected View prepareViewOfFilters(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.view_standard_filters, null, false);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        StandardFilters other = (StandardFilters) obj;
        return filters.equals(other.filters);
    }

    @Override
    public int hashCode() {
        return filters != null ? filters.hashCode() : 0;
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
