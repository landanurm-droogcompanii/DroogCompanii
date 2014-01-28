package ru.droogcompanii.application.view.activity_3.filter_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.util.LogUtils;
import ru.droogcompanii.application.view.fragment.filter_fragment.FilterFragment;
import ru.droogcompanii.application.view.fragment.filter_fragment.FilterSet;
import ru.droogcompanii.application.view.fragment.filter_fragment.FilterUtils;
import ru.droogcompanii.application.view.fragment.filter_fragment.filters.Filters;

/**
 * Created by ls on 15.01.14.
 */

public class FilterActivity extends FragmentActivity {

    public static final int REQUEST_CODE = 14235;

    private Bundle args;
    private Filters filtersDuringOpening;
    private FilterFragment filterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_filter);

        filterFragment = new FilterFragment();
        args = extractArguments(savedInstanceState);
        filterFragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (savedInstanceState == null) {
            fragmentTransaction.add(R.id.containerOfFilterFragment, filterFragment);
        } else {
            fragmentTransaction.replace(R.id.containerOfFilterFragment, filterFragment);
        }
        fragmentTransaction.commit();

        filtersDuringOpening = prepareFilters(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        filtersDuringOpening = prepareFilters(savedInstanceState);
    }

    private Bundle extractArguments(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return getIntent().getExtras();
        } else {
            return savedInstanceState.getBundle(Keys.args);
        }
    }

    private Filters prepareFilters(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return FilterUtils.getCurrentFilters(this, filterFragment.getPartnerCategory());
        } else {
            return readFiltersFrom(savedInstanceState);
        }
    }

    private Filters readFiltersFrom(Bundle savedInstanceState) {
        return (Filters) savedInstanceState.getSerializable(Keys.filters);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(Keys.args, args);
        outState.putSerializable(Keys.filters, filtersDuringOpening);
    }

    @Override
    public void onBackPressed() {
        if (changesWereMade()) {
            LogUtils.debug("WERE MADE");
            setResult();
        } else {
            LogUtils.debug("WERE NOT MADE");
            setResult(RESULT_CANCELED);
        }
        super.onBackPressed();
    }

    private boolean changesWereMade() {
        Filters currentFilters = filterFragment.getFilters();
        return !currentFilters.equals(filtersDuringOpening);
    }

    private void setResult() {
        Intent returnIntent = new Intent();
        FilterSet filterSet = filterFragment.getFilterSet();
        LogUtils.debug(filterSet == null ? "null" : "not null");
        returnIntent.putExtra(Keys.filterSet, (Serializable) filterSet);
        setResult(RESULT_OK, returnIntent);
    }
}
