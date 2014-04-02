package ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper;

import android.app.Activity;

import ru.droogcompanii.application.ui.screens.help.HelpActivity;

/**
 * Created by ls on 14.02.14.
 */
class ActionOnHelp implements MenuItemHelper.Action {
    @Override
    public void run(Activity activity) {
        HelpActivity.start(activity);
    }
}
