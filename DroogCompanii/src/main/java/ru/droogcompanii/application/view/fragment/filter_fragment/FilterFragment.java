package ru.droogcompanii.application.view.fragment.filter_fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.util.SharedPreferencesProvider;

/**
 * Created by ls on 21.01.14.
 */
public class FilterFragment extends android.support.v4.app.Fragment {

    private PartnerCategory partnerCategory;
    private SharedPreferences sharedPreferences;
    private Filters filters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            init(getArguments());
        } else {
            init(savedInstanceState);
        }
    }

    private void init(Bundle bundle) {
        sharedPreferences = SharedPreferencesProvider.get(getActivity());
        partnerCategory = extractPartnerCategoryFrom(bundle);
        filters = new Filters(partnerCategory);
        filters.restoreFrom(sharedPreferences);
        filters.displayOn(getView());
    }

    private PartnerCategory extractPartnerCategoryFrom(Bundle bundle) {
        if (bundle == null) {
            return null;
        } else {
            return (PartnerCategory) bundle.getSerializable(Keys.partnerCategory);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        filters.readFrom(getView());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        filters.saveInto(editor);
        editor.commit();
    }

    public FilterSet getFilterSet() {
        FilterSet filterSet = new FilterSetImpl();
        filters.readFrom(getView());
        filters.includeIn(filterSet);
        return filterSet;
    }
}
