package ru.droogcompanii.application.ui.activity_3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.util.Collection;
import java.util.Set;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity_3.filter_activity.FilterActivity;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterUtils;
import ru.droogcompanii.application.ui.fragment.partner_points_info_panel_fragment.PartnerPointsInfoPanelFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map_fragment.PartnerPointsMapFragment;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 22.01.14.
 */
public abstract class ActivityWithPartnerPointsMapFragmentAndInfoPanel
                        extends android.support.v4.app.FragmentActivity
                        implements PartnerPointsMapFragment.OnNeedToShowPartnerPointsListener {

    protected PartnerPointsMapFragment partnerPointsMapFragment;
    protected PartnerPointsInfoPanelFragment partnerPointsInfoPanelFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getRootLayoutId());

        FragmentManager fragmentManager = getSupportFragmentManager();
        partnerPointsMapFragment = (PartnerPointsMapFragment)
                fragmentManager.findFragmentById(R.id.mapFragment);
        partnerPointsInfoPanelFragment = (PartnerPointsInfoPanelFragment)
                fragmentManager.findFragmentById(R.id.partnerPointsInfoPanelFragment);

        onCreateActivity(savedInstanceState);

        if (savedInstanceState == null) {
            initPartnerPointsMapFragment();
        }

        findViewById(R.id.filterButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFilter();
            }
        });
    }

    protected abstract int getRootLayoutId();

    protected abstract void onCreateActivity(Bundle savedInstanceState);

    private void initPartnerPointsMapFragment() {
        partnerPointsMapFragment.setPartnerPoints(preparePartnerPoints());
        partnerPointsMapFragment.setFilterSet(FilterUtils.getCurrentFilterSet(this));
    }

    protected abstract Collection<PartnerPoint> preparePartnerPoints();

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
