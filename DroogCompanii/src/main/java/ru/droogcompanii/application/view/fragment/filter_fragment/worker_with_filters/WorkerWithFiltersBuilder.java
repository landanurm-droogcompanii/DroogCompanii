package ru.droogcompanii.application.view.fragment.filter_fragment.worker_with_filters;

import android.content.Context;

/**
 * Created by ls on 15.01.14.
 */
public interface WorkerWithFiltersBuilder<T> {
    WorkerWithFilters<T> build(Context context);
}
