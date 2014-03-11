package ru.droogcompanii.application.ui.activity.menu_helper.menu_item_helper;

import android.app.Activity;
import android.content.Intent;

import ru.droogcompanii.application.ui.activity.filter.FilterActivity;

/**
 * Created by ls on 14.02.14.
 */
class ActionOnFilter implements MenuItemHelper.Action {
    @Override
    public void run(Activity activity) {
        Intent intent = new Intent(activity, FilterActivity.class);
        activity.startActivityForResult(intent, FilterActivity.REQUEST_CODE);
    }
}
