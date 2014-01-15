package ru.droogcompanii.application.view.activity_3.filter_activity.filter;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * Created by ls on 15.01.14.
 */
public interface WorkerWithFilters<T> {
    View prepareViewOfFilters(Context context);
    List<Filter<T>> readFilters(View viewOfFilters);
}
