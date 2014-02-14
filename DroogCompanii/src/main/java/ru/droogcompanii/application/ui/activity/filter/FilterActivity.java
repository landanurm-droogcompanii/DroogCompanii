package ru.droogcompanii.application.ui.activity.filter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.activity.base_menu_helper.MenuHelper;
import ru.droogcompanii.application.ui.fragment.filter.FilterFragment;
import ru.droogcompanii.application.ui.fragment.filter.FilterUtils;
import ru.droogcompanii.application.ui.fragment.filter.filters.Filters;
import ru.droogcompanii.application.ui.helpers.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 15.01.14.
 */

public class FilterActivity extends ActionBarActivityWithUpButton {

    public static final int REQUEST_CODE = 14235;

    private boolean isFirstLaunched;
    private Bundle passedBundle;
    private Filters filtersAtTheMomentOfFirstLaunch;
    private FilterFragment filterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        isFirstLaunched = (savedInstanceState == null);

        passedBundle = (savedInstanceState == null)
                ? getPassedBundle() : savedInstanceState.getBundle(Keys.passedBundle);

        filterFragment = new FilterFragment();
        filterFragment.setArguments(passedBundle);

        placeFilterFragmentOnActivity();

        filtersAtTheMomentOfFirstLaunch = getFiltersAtTheMomentOfFirstLaunch(savedInstanceState);

    }

    private Bundle getPassedBundle() {
        return getIntent().getExtras();
    }

    private void placeFilterFragmentOnActivity() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (isFirstLaunched) {
            transaction.add(R.id.containerOfFilterFragment, filterFragment);
        } else {
            transaction.replace(R.id.containerOfFilterFragment, filterFragment);
        }
        transaction.commit();
    }

    private Filters getFiltersAtTheMomentOfFirstLaunch(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return getCurrentFilters();
        } else {
            return readFiltersFrom(savedInstanceState);
        }
    }

    private Filters getCurrentFilters() {
        PartnerCategory partnerCategory = filterFragment.getPartnerCategory();
        return FilterUtils.getCurrentFilters(this, partnerCategory);
    }

    private static Filters readFiltersFrom(Bundle savedInstanceState) {
        return (Filters) savedInstanceState.getSerializable(Keys.filtersAtTheMomentOfFirstLaunch);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(Keys.passedBundle, passedBundle);
        outState.putSerializable(Keys.filtersAtTheMomentOfFirstLaunch, filtersAtTheMomentOfFirstLaunch);
    }

    @Override
    public void finish() {
        if (wereChangesMade()) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        super.finish();
    }

    private boolean wereChangesMade() {
        Filters currentFilters = filterFragment.getFilters();
        return !currentFilters.equals(filtersAtTheMomentOfFirstLaunch);
    }
}
