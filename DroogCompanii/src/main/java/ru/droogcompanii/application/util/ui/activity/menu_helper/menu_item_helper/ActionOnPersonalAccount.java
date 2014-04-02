package ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper;

import android.app.Activity;

import ru.droogcompanii.application.ui.screens.personal_account.PersonalAccountActivity;

/**
 * Created by ls on 14.02.14.
 */
class ActionOnPersonalAccount implements MenuItemHelper.Action {
    @Override
    public void run(Activity activity) {
        PersonalAccountActivity.start(activity);
    }
}
