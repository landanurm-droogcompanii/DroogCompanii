package ru.droogcompanii.application.ui.activity.search_result_map_activity;

import android.os.Bundle;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.activity.activity_with_partner_points_map_fragment_and_info_panel.ActivityWithPartnerPointsMapFragmentAndInfoPanel;
import ru.droogcompanii.application.ui.fragment.partner_points_map_fragment.PartnerPointsProvider;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 14.01.14.
 */
public class SearchResultMapActivity extends ActivityWithPartnerPointsMapFragmentAndInfoPanel {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_search_result_map;
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        // do nothing
    }

    @Override
    protected PartnerPointsProvider preparePartnerPointsProvider() {
        Bundle args = getIntent().getExtras();
        return (PartnerPointsProvider) args.getSerializable(Keys.partnerPointsProvider);
    }
}
