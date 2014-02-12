package ru.droogcompanii.application.ui.activity.search_result_map;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.activity.base_with_partner_points_map_fragment_and_info_panel.BaseActivityWithPartnerPointsMapFragmentAndInfoPanel;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsProvider;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 14.01.14.
 */
public class SearchResultMapActivity extends BaseActivityWithPartnerPointsMapFragmentAndInfoPanel {

    @Override
    protected PartnerPointsProvider getPartnerPointsProvider() {
        Bundle args = getIntent().getExtras();
        return (PartnerPointsProvider) args.getSerializable(Keys.partnerPointsProvider);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        hideSearchItem(menu);
        return true;
    }

    private static void hideSearchItem(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);
    }

}
