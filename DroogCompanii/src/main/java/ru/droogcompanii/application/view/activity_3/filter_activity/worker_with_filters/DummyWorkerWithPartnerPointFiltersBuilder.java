package ru.droogcompanii.application.view.activity_3.filter_activity.worker_with_filters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.view.activity_3.filter_activity.filter.Filter;

/**
 * Created by ls on 15.01.14.
 */
public class DummyWorkerWithPartnerPointFiltersBuilder implements WorkerWithFiltersBuilder<PartnerPoint> {
    @Override
    public WorkerWithFilters<PartnerPoint> build(Context context) {
        return new WorkerWithFilters<PartnerPoint>() {
            @Override
            public View prepareViewOfFilters(Context context) {
                View noView = new TextView(context);
                return noView;
            }

            @Override
            public List<Filter<PartnerPoint>> readFilters(View viewOfFilters) {
                List<Filter<PartnerPoint>> noFilters = new ArrayList<Filter<PartnerPoint>>();
                return noFilters;
            }
        };
    }
}
