package ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.filter.Filter;
import ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.state.StandardFiltersState;
import ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.state.StandardFiltersStateDisplay;
import ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.state.StandardFiltersStateReader;
import ru.droogcompanii.application.view.activity_3.filter_activity.worker_with_filters.WorkerWithFilters;

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
        StandardFiltersState state = new StandardFiltersState();
        state.restoreFrom(sharedPreferences);
        StandardFiltersStateDisplay stateDisplay = new StandardFiltersStateDisplay(viewOfFilters);
        stateDisplay.display(state);
    }

    @Override
    public List<Filter<PartnerPoint>> readFilters(View viewOfFilters) {
        StandardFiltersReader filtersReader = new StandardFiltersReader(viewOfFilters);
        return filtersReader.read();
    }

    @Override
    public void saveInto(SharedPreferences.Editor editor, View viewOfFilters) {
        StandardFiltersStateReader standardFiltersStateReader = new StandardFiltersStateReader(viewOfFilters);
        StandardFiltersState state = standardFiltersStateReader.read();
        state.saveInto(editor);
    }
}
