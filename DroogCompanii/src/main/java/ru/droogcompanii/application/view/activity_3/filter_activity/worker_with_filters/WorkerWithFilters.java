package ru.droogcompanii.application.view.activity_3.filter_activity.worker_with_filters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import java.util.List;

import ru.droogcompanii.application.data.searchable_sortable_listing.filter.Filter;

/**
 * Created by ls on 15.01.14.
 */
public interface WorkerWithFilters<T> {
    View prepareViewOfFilters(Context context, SharedPreferences sharedPreferences);
    List<Filter<T>> readFilters(View viewOfFilters);
    void saveInto(SharedPreferences.Editor editor, View viewOfFilters);
}
