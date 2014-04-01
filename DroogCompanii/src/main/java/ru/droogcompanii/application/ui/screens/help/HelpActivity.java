package ru.droogcompanii.application.ui.screens.help;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.ui.activity.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.util.ui.activity.menu_helper.MenuHelper;
import ru.droogcompanii.application.util.ui.activity.menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper.MenuItemHelpers;

/**
 * Created by ls on 14.02.14.
 */
public class HelpActivity extends ActionBarActivityWithUpButton {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        if (savedInstanceState == null) {
            placeFragmentOnLayout();
        }
    }

    private void placeFragmentOnLayout() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new HelpFragment();
        String tag = fragment.getClass().getName();
        transaction.add(R.id.containerOfFragment, fragment, tag);
        transaction.commit();
    }

    @Override
    protected MenuHelper getMenuHelper() {
        return new MenuHelperItemsProvider(this) {
            @Override
            protected MenuItemHelper[] getMenuItemHelpers() {
                return new MenuItemHelper[] {
                        MenuItemHelpers.PERSONAL_ACCOUNT,
                        MenuItemHelpers.SETTINGS
                };
            }
        };
    }
}
