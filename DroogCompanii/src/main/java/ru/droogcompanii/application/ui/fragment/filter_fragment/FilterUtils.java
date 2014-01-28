package ru.droogcompanii.application.ui.fragment.filter_fragment;

import android.content.Context;
import android.content.SharedPreferences;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.util.SharedPreferencesProvider;
import ru.droogcompanii.application.ui.fragment.filter_fragment.filters.Filters;

/**
 * Created by ls on 22.01.14.
 */
public class FilterUtils {

    public static void resetFilters(Context context) {
        SharedPreferences sharedPreferences = SharedPreferencesProvider.get(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static FilterSet getCurrentFilterSet(Context context) {
        return getCurrentFilterSet(context, null);
    }

    public static FilterSet getCurrentFilterSet(Context context, PartnerCategory partnerCategory) {
        FilterSet filterSet = new FilterSetImpl();
        Filters currentFilters = getCurrentFilters(context, partnerCategory);
        currentFilters.includeIn(filterSet);
        return filterSet;
    }

    public static Filters getCurrentFilters(Context context, PartnerCategory partnerCategory) {
        SharedPreferences sharedPreferences = SharedPreferencesProvider.get(context);
        return new Filters(partnerCategory, sharedPreferences);
    }
}
