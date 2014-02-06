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
import ru.droogcompanii.application.ui.helpers.FragmentRemover;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 22.01.14.
 */
public abstract class ActivityWithPartnerPointsMapFragmentAndInfoPanel
            extends ActionBarActivityWithUpButton
            implements PartnerPointsMapFragment.Callbacks, TaskFragmentHolder.Callbacks {

    private boolean isTaskStarted;

    protected PartnerPointsMapFragment partnerPointsMapFragment;
    protected PartnerPointsInfoPanelFragment partnerPointsInfoPanelFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        isTaskStarted = getBooleanFlag(savedInstanceState, Keys.isTaskStarted, false);

        findFragments();

        if (savedInstanceState == null) {
            startTaskIfNotStarted();
        }

        setTitle();
    }

    private boolean getBooleanFlag(Bundle bundle, String key, boolean defaultValue) {
        if (bundle == null) {
            return defaultValue;
        }
        return bundle.getBoolean(key);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Keys.isTaskStarted, isTaskStarted);
    }

    private void findFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        partnerPointsMapFragment = (PartnerPointsMapFragment)
                fragmentManager.findFragmentById(R.id.mapFragment);
        partnerPointsInfoPanelFragment = (PartnerPointsInfoPanelFragment)
                fragmentManager.findFragmentById(R.id.partnerPointsInfoPanelFragment);
    }

    private void startTaskIfNotStarted() {
        if (!isTaskStarted) {
            startTask();
        }
        partnerPointsMapFragment.setDoNotInitOnResume();
    }

    private void startTask() {
        isTaskStarted = true;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment taskFragment = new PartnerPointsProviderTaskFragmentHolder();
        transaction.add(R.id.taskFragmentContainer, taskFragment);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMapIfDataWasUpdated();
    }

    private void updateMapIfDataWasUpdated() {
        if (FlagNeedToUpdateMap.isSet()) {
            startTaskIfNotStarted();
        }
    }

    private void initPartnerPointsMapAndInfoPanel(Collection<PartnerPoint> partnerPoints) {
        if (partnerPoints != null) {
            initPartnerPointsMapFragment(partnerPoints);
        }
    }

    private void initPartnerPointsMapFragment(Collection<PartnerPoint> partnerPoints) {
        partnerPointsMapFragment.setPartnerPoints(partnerPoints);
        partnerPointsMapFragment.setFilterSet(FilterUtils.getCurrentFilterSet(this));
        partnerPointsMapFragment.setNoClickedMarker();
    }

    @Override
    public void onTaskFinished(int resultCode, Serializable result) {
        isTaskStarted = false;
        if (resultCode == RESULT_OK) {
            onTaskFinishedSuccessfully(result);
        } else {
            onTaskCancelled();
        }
    }

    private void onTaskFinishedSuccessfully(Serializable result) {
        FragmentRemover.removeFragmentByContainerId(this, R.id.taskFragmentContainer);
        initPartnerPointsMapAndInfoPanel((List<PartnerPoint>) result);
        FlagNeedToUpdateMap.set(false);
    }

    private void onTaskCancelled() {
        finish();
    }

    private void setTitle() {
        PartnerPointsProvider partnerPointsProvider = getPartnerPointsProvider();
        String title = partnerPointsProvider.getTitle(this);
        setTitle(title);
    }

    protected abstract PartnerPointsProvider getPartnerPointsProvider();

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
            applyFilters();
        }
    }

    private void applyFilters() {
        FilterSet currentFilterSet = FilterUtils.getCurrentFilterSet(this);
        partnerPointsMapFragment.setFilterSet(currentFilterSet);
    }

    private void onSearch() {
        SearchActivity.start(this, SearchActivity.UsageType.SEARCH_BY_QUERY);
    }

    private void onMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
