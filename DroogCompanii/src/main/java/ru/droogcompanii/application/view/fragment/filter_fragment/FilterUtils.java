package ru.droogcompanii.application.view.fragment.filter_fragment;

import android.content.Context;
import android.content.SharedPreferences;

import ru.droogcompanii.application.SharedPreferencesProvider;
import ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.StandardFiltersReader;
import ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.state.StandardFiltersState;

/**
 * Created by ls on 21.01.14.
 */
public class FilterUtils {

    public static Filters getCurrentFilters(Context context) {
        SharedPreferences sharedPreferences = SharedPreferencesProvider.get(context);
        StandardFiltersReader filterReader = new StandardFiltersReader(sharedPreferences);
        return filterReader.read();
    }

    public static void restoreFiltersToDefault(Context context) {
        SharedPreferences.Editor editor = SharedPreferencesProvider.get(context).edit();
        StandardFiltersState.DEFAULT.saveInto(editor);
        editor.commit();
    }
}
