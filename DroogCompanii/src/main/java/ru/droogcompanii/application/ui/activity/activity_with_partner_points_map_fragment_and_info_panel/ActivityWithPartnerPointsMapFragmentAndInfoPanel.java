package ru.droogcompanii.application.ui.activity.activity_with_partner_points_map_fragment_and_info_panel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.filter_activity.FilterActivity;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterUtils;
import ru.droogcompanii.application.ui.fragment.partner_points_info_panel_fragment.PartnerPointsInfoPanelFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map_fragment.PartnerPointsMapFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map_fragment.PartnerPointsProvider;
import ru.droogcompanii.application.ui.helpers.TaskFragmentRemover;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 22.01.14.
 */
public abstract class ActivityWithPartnerPointsMapFragmentAndInfoPanel
            extends android.support.v4.app.FragmentActivity
            implements PartnerPointsMapFragment.OnNeedToShowPartnerPointsListener, TaskFragmentHolder.Callbacks {

    private boolean isFirstLaunched;
    private boolean isTaskFinished;

    protected PartnerPointsMapFragment partnerPointsMapFragment;
    protected PartnerPointsInfoPanelFragment partnerPointsInfoPanelFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        onCreateActivity(savedInstanceState);

        if (savedInstanceState == null) {
            isFirstLaunched = true;
            isTaskFinished = false;
        } else {
            isFirstLaunched = false;
            isTaskFinished = savedInstanceState.getBoolean(Keys.isTaskFinished);
        }

        if (isTaskFinished) {
            initPartnerPointsMapAndInfoPanel(null);
        } else {
            startTask();
        }
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

        findViewById(R.id.filterButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFilter();
            }
        });
    }

    @Override
    public void onTaskFinished(int resultCode, Serializable result) {
        if (resultCode == RESULT_CANCELED) {
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

    protected abstract int getContentViewId();

    protected abstract void onCreateActivity(Bundle savedInstanceState);

    private void initPartnerPointsMapFragment(Collection<PartnerPoint> partnerPoints) {
        partnerPointsMapFragment.setPartnerPoints(partnerPoints);
        partnerPointsMapFragment.setFilterSet(FilterUtils.getCurrentFilterSet(this));
    }

    protected abstract PartnerPointsProvider preparePartnerPointsProvider();

    protected void onFilter() {
        Intent intent = new Intent(this, FilterActivity.class);
        startActivityForResult(intent, FilterActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == FilterActivity.REQUEST_CODE) && (resultCode == RESULT_OK)) {
            FilterSet returnedFilterSet = extractReturnedFilters(data);
            applyFilters(returnedFilterSet);
        }
    }

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

    private void applyFilters(FilterSet filterSet) {
        partnerPointsMapFragment.setFilterSet(filterSet);
    }

    @Override
    public void onNeedToShowPartnerPoints(Set<PartnerPoint> partnerPointsToShow) {
        partnerPointsInfoPanelFragment.setPartnerPoints(partnerPointsToShow);
    }

    @Override
    public void onNoLongerNeedToShowPartnerPoints() {
        partnerPointsInfoPanelFragment.hide();
    }
}
