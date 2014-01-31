package ru.droogcompanii.application.ui.activity.filter_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterFragment;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterUtils;
import ru.droogcompanii.application.ui.fragment.filter_fragment.filters.Filters;

/**
 * Created by ls on 15.01.14.
 */

public class FilterActivity extends FragmentActivity {

    public static final int REQUEST_CODE = 14235;

    private Bundle args;
    private Filters filtersDuringCreation;
    private FilterFragment filterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

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

        filtersDuringCreation = prepareFiltersDuringCreation(savedInstanceState);
    }

    private Bundle extractArguments(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return getIntent().getExtras();
        } else {
            return savedInstanceState.getBundle(Keys.args);
        }
    }

    private Filters prepareFiltersDuringCreation(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            PartnerCategory partnerCategory = filterFragment.getPartnerCategory();
            return FilterUtils.getCurrentFilters(this, partnerCategory);
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
        outState.putSerializable(Keys.filters, filtersDuringCreation);
    }

    @Override
    public void onBackPressed() {
        if (changesWereMade()) {
            setResult(RESULT_OK, getResult());
        } else {
            setResult(RESULT_CANCELED);
        }
        super.onBackPressed();
    }

    private boolean changesWereMade() {
        Filters currentFilters = filterFragment.getFilters();
        return !currentFilters.equals(filtersDuringCreation);
    }

    private Intent getResult() {
        FilterSet filterSet = filterFragment.getFilterSet();
        Intent result = new Intent();
        result.putExtra(Keys.filterSet, (Serializable) filterSet);
        return result;
    }
}
