package ru.droogcompanii.application.ui.activity.help;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.activity.menu_helper.MenuHelper;
import ru.droogcompanii.application.ui.activity.menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.ui.activity.menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.ui.activity.menu_helper.menu_item_helper.MenuItemHelpers;
import ru.droogcompanii.application.ui.fragment.help.HelpFragment;
import ru.droogcompanii.application.ui.util.ActionBarActivityWithUpButton;

/**
 * Created by ls on 14.02.14.
 */
public class HelpActivity extends ActionBarActivityWithUpButton {

    private static final String TAG_OF_FRAGMENT = "TagOfHelpFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.containerOfFragment, new HelpFragment(), TAG_OF_FRAGMENT);
            transaction.commit();
        }
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
