package ru.droogcompanii.application.ui.activity.base_with_partner_points_map_fragment_and_info_panel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.global_flags.FlagNeedToUpdateMap;
import ru.droogcompanii.application.ui.activity.able_to_start_task.TaskNotBeInterrupted;
import ru.droogcompanii.application.ui.activity.filter.FilterActivity;
import ru.droogcompanii.application.ui.fragment.partner_points_info_panel.PartnerPointsInfoPanelFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsMapFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsProvider;
import ru.droogcompanii.application.ui.util.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 22.01.14.
 */
public abstract class BaseActivityWithPartnerPointsMapFragmentAndInfoPanel
            extends ActionBarActivityWithUpButton
            implements PartnerPointsMapFragment.Callbacks {

    private static final int TASK_REQUEST_CODE_PARTNER_POINTS_RECEIVING = 107;


    private boolean isTaskStarted;
    protected PartnerPointsMapFragment partnerPointsMapFragment;
    protected PartnerPointsInfoPanelFragment partnerPointsInfoPanelFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreState(savedInstanceState);
        }

        findFragments();

        if (savedInstanceState == null) {
            startTaskIfNotStarted();
        }

        setTitle();
    }

    private void initStateByDefault() {
        isTaskStarted = false;
    }

    private void restoreState(Bundle savedInstanceState) {
        isTaskStarted = savedInstanceState.getBoolean(Keys.isTaskStarted);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateInto(outState);
    }

    private void saveStateInto(Bundle outState) {
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
            startPartnerPointsReceivingTask();
        }
        partnerPointsMapFragment.setDoNotInitOnResume();
    }

    private void startPartnerPointsReceivingTask() {
        isTaskStarted = true;

        TaskNotBeInterrupted task = new PartnerPointsProviderTask(getPartnerPointsProvider(), this);

        startTask(TASK_REQUEST_CODE_PARTNER_POINTS_RECEIVING, task);
    }

    @Override
    protected void onReceiveResult(int requestCode, int resultCode, Serializable result) {
        if (requestCode == TASK_REQUEST_CODE_PARTNER_POINTS_RECEIVING) {
            onPartnerPointsReceivingTaskFinished(resultCode, result);
        }
    }

    private void onPartnerPointsReceivingTaskFinished(int resultCode, Serializable result) {
        isTaskStarted = false;
        if (resultCode != RESULT_OK) {
            finish();
        }
        onPartnerPointsReceivingTaskFinishedSuccessfully(result);
    }

    private void onPartnerPointsReceivingTaskFinishedSuccessfully(Serializable result) {
        initPartnerPointsMapFragmentIfNeed((List<PartnerPoint>) result);
        FlagNeedToUpdateMap.set(false);
    }

    private void initPartnerPointsMapFragmentIfNeed(Collection<PartnerPoint> partnerPoints) {
        if (partnerPoints != null) {
            initPartnerPointsMapFragment(partnerPoints);
        }
    }

    private void initPartnerPointsMapFragment(Collection<PartnerPoint> partnerPoints) {
        partnerPointsMapFragment.setPartnerPoints(partnerPoints);
        partnerPointsMapFragment.updateFilterSet();
        partnerPointsMapFragment.setNoClickedMarker();
    }

    private void setTitle() {
        String title = getPartnerPointsProvider().getTitle(this);
        setTitle(title);
    }

    protected abstract PartnerPointsProvider getPartnerPointsProvider();

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
        if ((requestCode == FilterActivity.REQUEST_CODE) && (resultCode == RESULT_OK)) {
            partnerPointsMapFragment.updateFilterSet();
        }
    }
}
