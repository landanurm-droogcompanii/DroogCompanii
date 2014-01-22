package ru.droogcompanii.application.view.fragment.filter_fragment.worker_with_filters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.view.fragment.filter_fragment.filters.Filters;

/**
 * Created by ls on 15.01.14.
 */
class DummyWorkerWithPartnerPointFilters implements WorkerWithFilters<PartnerPoint> {

    private static final DummyWorkerWithPartnerPointFilters
            SINGLE_INSTANCE = new DummyWorkerWithPartnerPointFilters();

    public static WorkerWithFilters<PartnerPoint> get() {
        return SINGLE_INSTANCE;
    }

    private DummyWorkerWithPartnerPointFilters() {
        // do nothing
    }

    @Override
    public View prepareViewOfFilters(Context context, SharedPreferences sharedPreferences) {
        View noView = new TextView(context);
        return noView;
    }

    @Override
    public Filters readFilters(View viewOfFilters) {
        Filters noFilters = new Filters();
        return noFilters;
    }

    @Override
    public void saveInto(SharedPreferences.Editor editor, View viewOfFilters) {
        // do nothing
    }
}
