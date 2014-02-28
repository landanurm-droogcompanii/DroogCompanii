package ru.droogcompanii.application.ui.activity.able_to_start_task;

import android.view.Menu;
import android.view.MenuItem;

import ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper.MenuItemIds;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;

/**
 * Created by ls on 21.02.14.
 */
public abstract class ActivityAbleToStartTaskWithGoToMapItem
        extends ActivityAbleToStartTask
        implements AbleToStartTask, TaskFragmentHolder.Callbacks {

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
