package ru.droogcompanii.application.ui.util.activity;

import android.view.Menu;
import android.view.MenuItem;

import ru.droogcompanii.application.ui.activity.menu_helper.menu_item_helper.MenuItemIds;

/**
 * Created by ls on 04.02.14.
 */
public abstract class ActionBarActivityWithUpButtonAndGoToMapItem extends ActionBarActivityWithUpButton {
    private MenuItem goToMapItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        goToMapItem = menu.findItem(MenuItemIds.GO_TO_MAP);
        goToMapItem.setVisible(isGoToMapItemVisible());
        return true;
    }

    protected abstract boolean isGoToMapItemVisible();

    protected void updateGoToMapItemVisible() {
        if (goToMapItem != null) {
            goToMapItem.setVisible(isGoToMapItemVisible());
        }
    }

}
