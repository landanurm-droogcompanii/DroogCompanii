package ru.droogcompanii.application.ui.activity.menu_helper.menu_item_helper;

import android.app.Activity;
import android.content.Intent;

import ru.droogcompanii.application.ui.activity.settings.SettingsActivity;

/**
 * Created by ls on 14.02.14.
 */
class ActionOnSettings implements MenuItemHelper.Action {
    @Override
    public void run(Activity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }
}
