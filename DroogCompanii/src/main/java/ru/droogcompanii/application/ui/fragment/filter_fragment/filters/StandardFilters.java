package ru.droogcompanii.application.ui.fragment.filter_fragment.filters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.fragment.filter_fragment.Filter;
import ru.droogcompanii.application.ui.fragment.filter_fragment.standard_filters.filter_impl.StandardFiltersProvider;

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
}
