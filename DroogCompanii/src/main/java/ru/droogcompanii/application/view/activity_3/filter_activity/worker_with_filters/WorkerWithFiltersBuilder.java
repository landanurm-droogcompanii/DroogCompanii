package ru.droogcompanii.application.view.activity_3.filter_activity.worker_with_filters;

import android.content.Context;

/**
 * Created by ls on 15.01.14.
 */
public interface WorkerWithFiltersBuilder<T> {
    WorkerWithFilters<T> build(Context context);
}
