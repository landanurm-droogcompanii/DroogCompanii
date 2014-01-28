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
import ru.droogcompanii.application.view.fragment.filter_fragment.filters.Filters;

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
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);
        if (savedInstanceState == null) {
            init(getArguments(), rootView);
        } else {
            init(savedInstanceState, rootView);
        }
        return rootView;
    }

    private void init(Bundle bundle, View rootView) {
        partnerCategory = readPartnerCategoryFrom(bundle);
        sharedPreferences = SharedPreferencesProvider.get(getActivity());
        filters = new Filters(partnerCategory);
        filters.restoreFrom(sharedPreferences);
        filters.displayOn(rootView);
    }

    private PartnerCategory readPartnerCategoryFrom(Bundle bundle) {
        if (bundle == null) {
            return null;
        } else {
            return (PartnerCategory) bundle.getSerializable(Keys.partnerCategory);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partnerCategory, partnerCategory);
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

    public Filters getFilters() {
        Filters currentFilters = new Filters(partnerCategory);
        currentFilters.readFrom(getView());
        return currentFilters;
    }

    public PartnerCategory getPartnerCategory() {
        return partnerCategory;
    }
}
