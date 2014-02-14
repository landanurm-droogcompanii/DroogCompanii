package ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper;

import android.app.Activity;

import ru.droogcompanii.application.ui.activity.search.SearchActivity;

/**
 * Created by ls on 14.02.14.
 */
class ActionOnSearch implements MenuItemHelper.Action {
    @Override
    public void run(Activity activity) {
        SearchActivity.start(activity, SearchActivity.UsageType.SEARCH_BY_QUERY);
    }
}
