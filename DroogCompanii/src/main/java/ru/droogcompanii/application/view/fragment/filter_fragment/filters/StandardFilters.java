package ru.droogcompanii.application.view.fragment.filter_fragment.filters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.view.fragment.filter_fragment.Filter;
import ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.filter_impl.StandardFiltersProvider;

/**
 * Created by ls on 24.01.14.
 */
public class StandardFilters extends BaseFilters {
    private final List<Filter> filters;

    public StandardFilters() {
        filters = StandardFiltersProvider.getDefaultFilters();
    }

    @Override
    protected List<Filter> getFilters() {
        return filters;
    }

    @Override
    protected View prepareViewOfFilters(View containerView) {
        Context context = containerView.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.view_standard_filters, null, false);
    }


}
