package ru.droogcompanii.application.ui.activity.menu_helper.menu_item_helper;

import android.app.Activity;
import android.content.Intent;

import ru.droogcompanii.application.ui.activity.search_2.SearchActivity2;

/**
 * Created by ls on 14.02.14.
 */
class ActionOnSearch implements MenuItemHelper.Action {
    @Override
    public void run(Activity activity) {
        Intent intent = new Intent(activity, SearchActivity2.class);
        activity.startActivity(intent);
    }
}
