package ru.droogcompanii.application.ui.activity.filter_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterFragment;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterUtils;
import ru.droogcompanii.application.ui.fragment.filter_fragment.filters.Filters;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 15.01.14.
 */

public class FilterActivity extends FragmentActivity {

    public static final int REQUEST_CODE = 14235;

    private Bundle args;
    private Filters filtersAtTheMomentOfFirstLaunch;
    private FilterFragment filterFragment;
    private boolean isFirstLaunched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        isFirstLaunched = (savedInstanceState == null);
        args = isFirstLaunched ? getPassedBundle() : savedInstanceState;

        filterFragment = new FilterFragment();
        filterFragment.setArguments(args);

        placeFilterFragmentOnActivity();

        filtersAtTheMomentOfFirstLaunch = getFiltersAtTheMomentOfFirstLaunch();
    }

    private Bundle getPassedBundle() {
        return getIntent().getExtras();
    }

    private void placeFilterFragmentOnActivity() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isFirstLaunched) {
            fragmentTransaction.add(R.id.containerOfFilterFragment, filterFragment);
        } else {
            fragmentTransaction.replace(R.id.containerOfFilterFragment, filterFragment);
        }
        fragmentTransaction.commit();
    }

    private Filters getFiltersAtTheMomentOfFirstLaunch() {
        if (isFirstLaunched) {
            return getCurrentFilters();
        } else {
            return readFiltersFrom(args);
        }
    }

    private Filters getCurrentFilters() {
        PartnerCategory partnerCategory = filterFragment.getPartnerCategory();
        return FilterUtils.getCurrentFilters(this, partnerCategory);
    }

    private Filters readFiltersFrom(Bundle savedInstanceState) {
        return (Filters) savedInstanceState.getSerializable(Keys.filters);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(Keys.args, args);
        outState.putSerializable(Keys.filters, filtersAtTheMomentOfFirstLaunch);
    }

    @Override
    public void onBackPressed() {
        if (wereChangesMade()) {
            setResult(RESULT_OK, getResult());
        } else {
            setResult(RESULT_CANCELED);
        }
        super.onBackPressed();
    }

    private boolean wereChangesMade() {
        Filters currentFilters = filterFragment.getFilters();
        return !currentFilters.equals(filtersAtTheMomentOfFirstLaunch);
    }

    private Intent getResult() {
        FilterSet filterSet = filterFragment.getFilterSet();
        Intent result = new Intent();
        result.putExtra(Keys.filterSet, (Serializable) filterSet);
        return result;
    }

}
