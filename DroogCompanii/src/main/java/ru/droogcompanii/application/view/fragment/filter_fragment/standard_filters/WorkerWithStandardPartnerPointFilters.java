package ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.view.fragment.filter_fragment.filters.Filter;
import ru.droogcompanii.application.view.fragment.filter_fragment.filters.Filters;
import ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.filter_impl.StandardFiltersUtils;
import ru.droogcompanii.application.view.fragment.filter_fragment.worker_with_filters.WorkerWithFilters;

/**
 * Created by ls on 15.01.14.
 */
public class WorkerWithStandardPartnerPointFilters implements WorkerWithFilters<PartnerPoint>, Serializable {

    @Override
    public View prepareViewOfFilters(Context context, SharedPreferences sharedPreferences) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewOfFilters = inflater.inflate(R.layout.view_standard_filters, null, false);
        init(viewOfFilters, sharedPreferences);
        return viewOfFilters;
    }

    private void init(View viewOfFilters, SharedPreferences sharedPreferences) {
        List<Filter> currentFilters = StandardFiltersUtils.getFiltersRestoredFrom(sharedPreferences);
        for (Filter filter : currentFilters) {
            filter.displayOn(viewOfFilters);
        }
    }

    @Override
    public Filters readFilters(View viewOfFilters) {
        List<Filter> currentFilters = StandardFiltersUtils.getFiltersReadedFrom(viewOfFilters);
        return Filters.from(currentFilters);
    }

    @Override
    public void saveInto(SharedPreferences.Editor editor, View viewOfFilters) {
        List<Filter> currentFilters = StandardFiltersUtils.getFiltersReadedFrom(viewOfFilters);
        for (Filter filter : currentFilters) {
            filter.saveInto(editor);
        }
    }
}
