package ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper;

import android.app.Activity;

import ru.droogcompanii.application.ui.activity.search_result_map.SearchResultMapActivity;
import ru.droogcompanii.application.ui.util.PartnerPointsProviderHolder;

/**
 * Created by ls on 14.02.14.
 */
class ActionOnGoToMap implements MenuItemHelper.Action {
    @Override
    public void run(Activity activity) {
        PartnerPointsProviderHolder providerHolder = (PartnerPointsProviderHolder) activity;
        SearchResultMapActivity.start(activity, providerHolder.getPartnerPointsProvider());
    }
}
