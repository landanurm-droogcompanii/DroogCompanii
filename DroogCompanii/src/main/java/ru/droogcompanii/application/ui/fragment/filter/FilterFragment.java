package ru.droogcompanii.application.ui.fragment.filter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.fragment.filter.filters.Filters;

/**
 * Created by ls on 21.01.14.
 */
public class FilterFragment extends android.support.v4.app.Fragment {

    public static interface Callbacks {
        Bundle getArguments();
    }

    private static final String KEY_DISPLAYED_FILTERS = "KEY_DISPLAYED_FILTERS";

    private static final String KEY_PARTNER_CATEGORY = "KEY_PARTNER_CATEGORY";

    private Callbacks callbacks;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            onFirstLaunch();
        } else {
            onRelaunch(savedInstanceState);
        }
    }

    private void onFirstLaunch() {
        Filters filters = FilterUtils.getCurrentFilters(getActivity(), getPartnerCategory());
        filters.displayOn(getView());
    }

    private void onRelaunch(Bundle savedInstanceState) {
        Filters filters = (Filters) savedInstanceState.getSerializable(KEY_DISPLAYED_FILTERS);
        filters.displayOn(getView());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_DISPLAYED_FILTERS, getDisplayedFilters());
    }

    public Filters getDisplayedFilters() {
        Filters currentFilters = new Filters(getPartnerCategory());
        currentFilters.readFrom(getView());
        return currentFilters;
    }

    public PartnerCategory getPartnerCategory() {
        Bundle args = callbacks.getArguments();
        if (args == null) {
            return null;
        } else {
            return (PartnerCategory) args.getSerializable(KEY_PARTNER_CATEGORY);
        }
    }
}
