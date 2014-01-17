package ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.view.activity_3.filter_activity.filter.Filter;
import ru.droogcompanii.application.view.activity_3.filter_activity.worker_with_filters.WorkerWithFilters;

/**
 * Created by ls on 15.01.14.
 */
public class WorkerWithStandardPartnerPointFilters implements WorkerWithFilters<PartnerPoint>, Serializable {

    @Override
    public View prepareViewOfFilters(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewOfFilters = inflater.inflate(R.layout.view_standard_filters, null, false);
        init(context, viewOfFilters);
        return viewOfFilters;
    }

    private void init(Context context, View viewOfFilters) {
        // TODO: may be need to read data from DB and init widgets on view
    }

    @Override
    public List<Filter<PartnerPoint>> readFilters(View viewOfFilters) {
        StandardFiltersReader filtersReader = new StandardFiltersReader(viewOfFilters);
        return filtersReader.read();
    }
}
