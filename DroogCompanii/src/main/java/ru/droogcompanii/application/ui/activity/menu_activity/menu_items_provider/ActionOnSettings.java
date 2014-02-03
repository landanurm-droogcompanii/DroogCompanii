package ru.droogcompanii.application.ui.activity.menu_activity.menu_items_provider;

import android.content.Context;
import android.content.Intent;

import ru.droogcompanii.application.ui.activity.menu_activity.MenuListItem;
import ru.droogcompanii.application.ui.activity.settings_activity.SettingsActivity;

/**
 * Created by ls on 31.01.14.
 */
class ActionOnSettings implements MenuListItem.Action {
    @Override
    public void launch(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }
}
