package ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.filter_impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import java.util.List;

import ru.droogcompanii.application.util.SharedPreferencesProvider;
import ru.droogcompanii.application.view.fragment.filter_fragment.filters.Filter;
import ru.droogcompanii.application.view.fragment.filter_fragment.filters.Filters;

/**
 * Created by ls on 22.01.14.
 */
public class StandardFiltersUtils {

    public static List<Filter> getFiltersRestoredFrom(SharedPreferences sharedPreferences) {
        List<Filter> standardFilters = StandardFiltersProvider.getDefaultFilters();
        for (Filter filter : standardFilters) {
            filter.restoreFrom(sharedPreferences);
        }
        return standardFilters;
    }

    public static List<Filter> getFiltersReadedFrom(View viewOfFilters) {
        List<Filter> filters = StandardFiltersProvider.getDefaultFilters();
        for (Filter filter : filters) {
            filter.readFrom(viewOfFilters);
        }
        return filters;
    }

    public static void setDefaultFilters(Context context) {
        SharedPreferences sharedPreferences = SharedPreferencesProvider.get(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<Filter> defaultStandardFilters = StandardFiltersProvider.getDefaultFilters();
        for (Filter defaultFilter : defaultStandardFilters) {
            defaultFilter.saveInto(editor);
        }
        editor.commit();
    }

    public static Filters getCurrentFilters(Context context) {
        SharedPreferences sharedPreferences = SharedPreferencesProvider.get(context);
        return Filters.from(getFiltersRestoredFrom(sharedPreferences));
    }
}
