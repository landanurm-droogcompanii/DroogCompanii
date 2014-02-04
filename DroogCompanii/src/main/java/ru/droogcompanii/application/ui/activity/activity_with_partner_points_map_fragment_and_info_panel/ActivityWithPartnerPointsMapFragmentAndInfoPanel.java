package ru.droogcompanii.application.ui.activity.activity_with_partner_points_map_fragment_and_info_panel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.filter_activity.FilterActivity;
import ru.droogcompanii.application.ui.activity.menu_activity.MenuActivity;
import ru.droogcompanii.application.ui.activity.search_activity.SearchActivity;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterUtils;
import ru.droogcompanii.application.ui.fragment.partner_points_info_panel_fragment.PartnerPointsInfoPanelFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map_fragment.PartnerPointsMapFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map_fragment.PartnerPointsProvider;
import ru.droogcompanii.application.ui.helpers.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.ui.helpers.TaskFragmentRemover;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 22.01.14.
 */
public abstract class ActivityWithPartnerPointsMapFragmentAndInfoPanel
            extends ActionBarActivityWithUpButton
            implements PartnerPointsMapFragment.Callbacks, TaskFragmentHolder.Callbacks {

    private boolean isFirstLaunched;
    private boolean isTaskFinished;

    protected PartnerPointsMapFragment partnerPointsMapFragment;
    protected PartnerPointsInfoPanelFragment partnerPointsInfoPanelFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        if (savedInstanceState == null) {
            isFirstLaunched = true;
            isTaskFinished = false;
        } else {
            isFirstLaunched = false;
            isTaskFinished = savedInstanceState.getBoolean(Keys.isTaskFinished);
        }

        if (isFirstLaunched) {
            startTask();
        }
        if (isTaskFinished) {
            initPartnerPointsMapAndInfoPanel(null);
        }

        initTitle();
    }

    private void startTask() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment taskFragment = new PartnerPointsProviderTaskFragmentHolder();
        transaction.add(R.id.taskFragmentContainer, taskFragment);
        transaction.commit();
    }

    private void initPartnerPointsMapAndInfoPanel(Collection<PartnerPoint> partnerPoints) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        partnerPointsMapFragment = (PartnerPointsMapFragment)
                fragmentManager.findFragmentById(R.id.mapFragment);
        partnerPointsInfoPanelFragment = (PartnerPointsInfoPanelFragment)
                fragmentManager.findFragmentById(R.id.partnerPointsInfoPanelFragment);

        if (partnerPoints != null) {
            initPartnerPointsMapFragment(partnerPoints);
        }
    }

    @Override
    public void onTaskFinished(int resultCode, Serializable result) {
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        isTaskFinished = true;
        TaskFragmentRemover.remove(this, R.id.taskFragmentContainer);
        List<PartnerPoint> partnerPoints = (List<PartnerPoint>) result;
        initPartnerPointsMapAndInfoPanel(partnerPoints);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Keys.isTaskFinished, isTaskFinished);
    }

    public boolean isFirstLaunched() {
        return isFirstLaunched;
    }


    private void initPartnerPointsMapFragment(Collection<PartnerPoint> partnerPoints) {
        partnerPointsMapFragment.setPartnerPoints(partnerPoints);
        partnerPointsMapFragment.setFilterSet(FilterUtils.getCurrentFilterSet(this));
    }

    private void initTitle() {
        PartnerPointsProvider partnerPointsProvider = getPartnerPointsProvider();
        setTitle(partnerPointsProvider.getTitle(this));
    }

    protected abstract PartnerPointsProvider getPartnerPointsProvider();

    private FilterSet extractReturnedFilters(Intent data) {
        FilterSet filterSet = null;
        if (data != null) {
            filterSet = (FilterSet) data.getSerializableExtra(Keys.filterSet);
        }
        if (filterSet == null) {
            throw new RuntimeException(FilterActivity.class.getName() + " doesn't return filterSet");
        }
        return filterSet;
    }

    @Override
    public void onNeedToShowPartnerPoints(Set<PartnerPoint> partnerPointsToShow) {
        partnerPointsInfoPanelFragment.setPartnerPoints(partnerPointsToShow);
    }

    @Override
    public void onNoLongerNeedToShowPartnerPoints() {
        partnerPointsInfoPanelFragment.hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filters:
                onFilter();
                return true;
            case R.id.action_search:
                onSearch();
                return true;
            case R.id.action_menu:
                onMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onFilter() {
        Intent intent = new Intent(this, FilterActivity.class);
        startActivityForResult(intent, FilterActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == FilterActivity.REQUEST_CODE) && (resultCode == RESULT_OK)) {
            applyFilters(data);
        }
    }

    private void applyFilters(Intent data) {
        FilterSet returnedFilterSet = extractReturnedFilters(data);
        partnerPointsMapFragment.setFilterSet(returnedFilterSet);
    }

    private void onSearch() {
        SearchActivity.start(this, SearchActivity.UsageType.SEARCH_BY_QUERY);
    }

    private void onMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
