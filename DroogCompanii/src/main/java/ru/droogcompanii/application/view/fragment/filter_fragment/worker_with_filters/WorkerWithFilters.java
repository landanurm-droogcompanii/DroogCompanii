package ru.droogcompanii.application.view.fragment.filter_fragment.worker_with_filters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import ru.droogcompanii.application.view.fragment.filter_fragment.filters.Filters;

/**
 * Created by ls on 15.01.14.
 */
public interface WorkerWithFilters<T> {
    View prepareViewOfFilters(Context context, SharedPreferences sharedPreferences);
    Filters readFilters(View viewOfFilters);
    void saveInto(SharedPreferences.Editor editor, View viewOfFilters);
}
