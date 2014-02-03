package ru.droogcompanii.application.ui.fragment.filter_fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.util.SharedPreferencesProvider;
import ru.droogcompanii.application.ui.fragment.filter_fragment.filters.Filters;

/**
 * Created by ls on 21.01.14.
 */
public class FilterFragment extends android.support.v4.app.Fragment {

    private PartnerCategory partnerCategory;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);
        Bundle bundle = (savedInstanceState == null) ? getArguments() : savedInstanceState;
        init(bundle, rootView);
        return rootView;
    }

    private void init(Bundle bundle, View rootView) {
        partnerCategory = readPartnerCategoryFrom(bundle);
        sharedPreferences = SharedPreferencesProvider.get(getActivity());
        displaySavedFiltersOn(rootView);
    }

    private PartnerCategory readPartnerCategoryFrom(Bundle bundle) {
        return (PartnerCategory) bundle.getSerializable(Keys.partnerCategory);
    }

    private void displaySavedFiltersOn(View view) {
        Filters filters = new Filters(partnerCategory);
        filters.restoreFrom(sharedPreferences);
        filters.displayOn(view);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partnerCategory, partnerCategory);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        getFilters().saveInto(editor);
        editor.commit();
    }

    public FilterSet getFilterSet() {
        FilterSet filterSet = new FilterSetImpl();
        getFilters().includeIn(filterSet);
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
