package ru.droogcompanii.application.ui.fragment.filter;

import android.content.Context;
import android.content.SharedPreferences;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.fragment.filter.filters.Filters;
import ru.droogcompanii.application.util.SharedPreferencesProvider;

/**
 * Created by ls on 22.01.14.
 */
public class FilterUtils {

    public static void resetFilters(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void shareFilters(Context context, Filters filters) {
        SharedPreferences.Editor editor = SharedPreferencesProvider.get(context).edit();
        editor.clear();
        filters.saveInto(editor);
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
        return new Filters(partnerCategory, getSharedPreferences(context));
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return SharedPreferencesProvider.get(context);
    }
}
