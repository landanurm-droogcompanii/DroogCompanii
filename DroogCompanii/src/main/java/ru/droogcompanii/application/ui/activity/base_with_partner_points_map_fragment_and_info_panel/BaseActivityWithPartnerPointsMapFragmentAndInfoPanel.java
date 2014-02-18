package ru.droogcompanii.application.ui.activity.base_with_partner_points_map_fragment_and_info_panel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.global_flags.FlagNeedToUpdateMap;
import ru.droogcompanii.application.ui.activity.filter.FilterActivity;
import ru.droogcompanii.application.ui.fragment.filter.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter.FilterUtils;
import ru.droogcompanii.application.ui.fragment.partner_points_info_panel.PartnerPointsInfoPanelFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsMapFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsProvider;
import ru.droogcompanii.application.ui.helpers.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.ui.helpers.FragmentRemover;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by ls on 22.01.14.
 */
public abstract class BaseActivityWithPartnerPointsMapFragmentAndInfoPanel
            extends ActionBarActivityWithUpButton
            implements PartnerPointsMapFragment.Callbacks, TaskFragmentHolder.Callbacks {

    private static final String TAG_TASK_FRAGMENT_HOLDER =
            "TaskFragmentHolder" + BaseActivityWithPartnerPointsMapFragmentAndInfoPanel.class.getName();


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
        transaction.add(R.id.taskFragmentContainer, taskFragment, TAG_TASK_FRAGMENT_HOLDER);
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
        LogUtils.debug("onTaskFinishedSuccessfully()");

        FragmentRemover.removeFragmentByTag(this, TAG_TASK_FRAGMENT_HOLDER);
        initPartnerPointsMapFragmentIfNeed((List<PartnerPoint>) result);
        FlagNeedToUpdateMap.set(false);
    }

    private void initPartnerPointsMapFragmentIfNeed(Collection<PartnerPoint> partnerPoints) {
        if (partnerPoints != null) {
            initPartnerPointsMapFragment(partnerPoints);
        }
    }

    private void onTaskCancelled() {
        LogUtils.debug("onTaskCancelled()");

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.debug("onActivityResult");

        if ((requestCode == FilterActivity.REQUEST_CODE) && (resultCode == RESULT_OK)) {
            applyFilters();
        }
    }

    private void applyFilters() {
        LogUtils.debug("applyFilters");

        FilterSet currentFilterSet = FilterUtils.getCurrentFilterSet(this);
        partnerPointsMapFragment.setFilterSet(currentFilterSet);
    }
}
