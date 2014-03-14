package ru.droogcompanii.application.ui.activity.base_with_partner_points_map_fragment_and_info_panel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.filter.FilterActivity;
import ru.droogcompanii.application.ui.activity.synchronization.SynchronizationActivity;
import ru.droogcompanii.application.ui.fragment.partner_points_info_panel.PartnerPointsInfoPanelFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map.NotifierAboutBaseMapLocationChanges;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsMapFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsProvider;
import ru.droogcompanii.application.ui.util.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.ui.util.CustomBaseLocationUtils;
import ru.droogcompanii.application.ui.util.LocationUtils;
import ru.droogcompanii.application.ui.util.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;

/**
 * Created by ls on 22.01.14.
 */
public abstract class BaseActivityWithPartnerPointsMapFragmentAndInfoPanel
            extends ActionBarActivityWithUpButton
            implements PartnerPointsMapFragment.Callbacks {

    private static final int TASK_REQUEST_CODE_PARTNER_POINTS_RECEIVING = 107;

    private static final String KEY_IS_TASK_STARTED = "KEY_IS_TASK_STARTED";

    private boolean isTaskStarted;
    protected PartnerPointsMapFragment partnerPointsMapFragment;
    protected PartnerPointsInfoPanelFragment partnerPointsInfoPanelFragment;
    private View dismissCustomBaseLocationActionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_partner_points_map_fragment_and_info_panel);

        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreState(savedInstanceState);
        }

        initView();

        if (savedInstanceState == null) {
            startPartnerPointsReceivingTaskIfNotStarted();
        }

        setTitle();
    }

    private void initStateByDefault() {
        isTaskStarted = false;
    }

    private void restoreState(Bundle savedInstanceState) {
        isTaskStarted = savedInstanceState.getBoolean(KEY_IS_TASK_STARTED);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateInto(outState);
    }

    private void saveStateInto(Bundle outState) {
        outState.putBoolean(KEY_IS_TASK_STARTED, isTaskStarted);
    }

    private void initView() {
        findFragments();
        initDismissCustomBaseLocationActionView();
    }

    private void initDismissCustomBaseLocationActionView() {
        dismissCustomBaseLocationActionView = findViewById(R.id.dismissCustomBaseLocationAction);
        dismissCustomBaseLocationActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDismissCustomBaseLocationView();
            }
        });
        int visibility = CustomBaseLocationUtils.isBasePositionSet() ? View.VISIBLE : View.INVISIBLE;
        dismissCustomBaseLocationActionView.setVisibility(visibility);
    }

    private void findFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        partnerPointsMapFragment = (PartnerPointsMapFragment)
                fragmentManager.findFragmentById(R.id.mapFragment);
        partnerPointsInfoPanelFragment = (PartnerPointsInfoPanelFragment)
                fragmentManager.findFragmentById(R.id.partnerPointsInfoPanelFragment);
    }

    private void startPartnerPointsReceivingTaskIfNotStarted() {
        if (!isTaskStarted) {
            startPartnerPointsReceivingTask();
        }
        partnerPointsMapFragment.setDoNotInitOnResume();
    }

    private void startPartnerPointsReceivingTask() {
        isTaskStarted = true;

        TaskNotBeInterruptedDuringConfigurationChange task = new PartnerPointsProviderTask(getPartnerPointsProvider(), this);

        startTask(TASK_REQUEST_CODE_PARTNER_POINTS_RECEIVING, task);
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
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
    public void onNeedToShowPartnerPoints(Set<PartnerPoint> partnerPointsToShow) {
        partnerPointsInfoPanelFragment.setPartnerPoints(partnerPointsToShow);
    }

    @Override
    public void onNoLongerNeedToShowPartnerPoints() {
        partnerPointsInfoPanelFragment.hide();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SynchronizationActivity.REQUEST_CODE) {
            onReturningFromSynchronizationActivity(resultCode);
        } else if ((requestCode == FilterActivity.REQUEST_CODE) && (resultCode == RESULT_OK)) {
            onFiltersChanged();
        }
    }

    private void onReturningFromSynchronizationActivity(int resultCode) {
        if (resultCode == RESULT_OK) {
            startPartnerPointsReceivingTaskIfNotStarted();
        }
    }

    private void onFiltersChanged() {
        partnerPointsMapFragment.updateFilterSet();
    }

    @Override
    public void onCustomBaseLocationIsSet() {
        setVisibilityOfDismissCustomBaseLocationActionView(View.VISIBLE);
    }

    private void setVisibilityOfDismissCustomBaseLocationActionView(int visibility) {
        dismissCustomBaseLocationActionView.setVisibility(visibility);
    }

    private void onDismissCustomBaseLocationView() {
        CustomBaseLocationUtils.dismissBasePosition();
        LocationUtils.notifyListeners();
        setVisibilityOfDismissCustomBaseLocationActionView(View.INVISIBLE);
        NotifierAboutBaseMapLocationChanges.notify(this);
    }
}
