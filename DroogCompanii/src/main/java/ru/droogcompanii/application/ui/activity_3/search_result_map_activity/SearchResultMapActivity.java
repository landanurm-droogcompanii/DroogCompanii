package ru.droogcompanii.application.ui.activity_3.search_result_map_activity;

import android.os.Bundle;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.activity_3.ActivityWithPartnerPointsMapFragmentAndInfoPanel;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterUtils;
import ru.droogcompanii.application.ui.fragment.partner_points_map_fragment.PartnerPointsProvider;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 14.01.14.
 */
public class SearchResultMapActivity extends ActivityWithPartnerPointsMapFragmentAndInfoPanel {

    @Override
    protected int getRootLayoutId() {
        return R.layout.activity_3_search_result_map;
    }

    @Override
    protected void onFirstActivityLaunch() {
        initPartnerPointsMapFragment();
    }

    private void initPartnerPointsMapFragment() {
        Bundle args = getIntent().getExtras();
        PartnerPointsProvider partnerPointsProvider =
                (PartnerPointsProvider) args.getSerializable(Keys.partnerPointsProvider);
        partnerPointsMapFragment.setPartnerPointsProvider(partnerPointsProvider);
        partnerPointsMapFragment.setFilterSet(FilterUtils.getCurrentFilterSet(this));
    }

    @Override
    protected void onEachActivityLaunch() {
        // do nothing
    }
}
