package ru.droogcompanii.application.ui.activity.menu_activity.menu_items_provider;

import android.content.Context;
import android.content.Intent;

import ru.droogcompanii.application.ui.activity.synchronization_activity.SynchronizationActivity;
import ru.droogcompanii.application.ui.activity.menu_activity.MenuListItem;

/**
 * Created by ls on 31.01.14.
 */
class ActionOnSynchronization implements MenuListItem.Action {
    @Override
    public void run(Context context) {
        Intent intent = new Intent(context, SynchronizationActivity.class);
        context.startActivity(intent);
    }
}
