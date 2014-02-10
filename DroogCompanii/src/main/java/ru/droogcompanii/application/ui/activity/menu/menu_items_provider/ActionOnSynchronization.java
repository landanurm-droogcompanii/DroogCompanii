package ru.droogcompanii.application.ui.activity.menu.menu_items_provider;

import android.content.Context;
import android.content.Intent;

import ru.droogcompanii.application.ui.activity.synchronization.SynchronizationActivity;
import ru.droogcompanii.application.ui.activity.menu.MenuListItem;

/**
 * Created by ls on 31.01.14.
 */
class ActionOnSynchronization implements MenuListItem.Action {
    @Override
    public void launch(Context context) {
        Intent intent = new Intent(context, SynchronizationActivity.class);
        context.startActivity(intent);
    }
}
