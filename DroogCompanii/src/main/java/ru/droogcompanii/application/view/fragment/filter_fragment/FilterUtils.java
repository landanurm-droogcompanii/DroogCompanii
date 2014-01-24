package ru.droogcompanii.application.view.fragment.filter_fragment;

import android.content.Context;
import android.content.SharedPreferences;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.util.SharedPreferencesProvider;

/**
 * Created by ls on 22.01.14.
 */
public class FilterUtils {

    public static void setDefaultFilters(Context context) {
        SharedPreferences sharedPreferences = SharedPreferencesProvider.get(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static FilterSet getCurrentFilterSet(Context context) {
        return getCurrentFilterSet(context, null);
    }

    public static FilterSet getCurrentFilterSet(Context context, PartnerCategory partnerCategory) {
        SharedPreferences sharedPreferences = SharedPreferencesProvider.get(context);
        FilterSet filterSet = new FilterSetImpl();
        Filters filters = new Filters(partnerCategory, sharedPreferences);
        filters.includeIn(filterSet);
        return filterSet;
    }
}
