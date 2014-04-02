package ru.droogcompanii.application.ui.screens.settings;

import android.content.Context;
import android.content.Intent;

import ru.droogcompanii.application.util.ui.activity.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.util.ui.activity.ReuseAlreadyLaunchedActivityFlag;
import ru.droogcompanii.application.util.ui.activity.menu_helper.MenuHelper;
import ru.droogcompanii.application.util.ui.activity.menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper.MenuItemHelpers;

/**
 * Created by ls on 31.01.14.
 */
public class SettingsActivity extends ActionBarActivityWithUpButton {

    public static void start(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        ReuseAlreadyLaunchedActivityFlag.set(intent);
        context.startActivity(intent);
    }

    @Override
    protected MenuHelper getMenuHelper() {
        return new MenuHelperItemsProvider(this) {
            @Override
            protected MenuItemHelper[] getMenuItemHelpers() {
                return new MenuItemHelper[] {
                        MenuItemHelpers.PERSONAL_ACCOUNT,
                        MenuItemHelpers.HELP
                };
            }
        };
    }
}
