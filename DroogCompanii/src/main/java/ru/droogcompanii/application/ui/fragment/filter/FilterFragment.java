package ru.droogcompanii.application.ui.fragment.filter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.fragment.filter.filters.Filters;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.util.SharedPreferencesProvider;

/**
 * Created by ls on 21.01.14.
 */
public class FilterFragment extends android.support.v4.app.Fragment {

    private SharedPreferences sharedPreferences;
    private PartnerCategory partnerCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);
        Bundle bundle = (savedInstanceState == null) ? getArguments() : savedInstanceState;
        init(bundle, rootView);
        return rootView;
    }

    private void init(Bundle bundle, View rootView) {
        partnerCategory = getPartnerCategory(bundle);
        sharedPreferences = SharedPreferencesProvider.get(getActivity());
        displaySavedFiltersOn(rootView);
    }

    private PartnerCategory getPartnerCategory(Bundle bundle) {
        if (bundle == null) {
            return null;
        } else {
            return (PartnerCategory) bundle.getSerializable(Keys.partnerCategory);
        }
    }

    private void displaySavedFiltersOn(View view) {
        Filters filters = new Filters(partnerCategory);
        filters.restoreFrom(sharedPreferences);
        filters.displayOn(view);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partnerCategory, (Serializable) partnerCategory);
    }

    @Override
    public void onPause() {
        super.onPause();
        saveFilters();
    }

    private void saveFilters() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        getFilters().saveInto(editor);
        editor.commit();
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
