package ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper;

import android.app.Activity;

import ru.droogcompanii.application.ui.screens.search.SearchActivity;

/**
 * Created by ls on 14.02.14.
 */
class ActionOnSearch implements MenuItemHelper.Action {
    @Override
    public void run(Activity activity) {
        SearchActivity.start(activity);
    }
}
