package ru.droogcompanii.application.ui.activity.menu.menu_items_provider;

import android.content.Context;

import ru.droogcompanii.application.ui.activity.menu.MenuListItem;
import ru.droogcompanii.application.ui.helpers.Caller;
import ru.droogcompanii.application.util.HotlineNumberProvider;

/**
 * Created by ls on 31.01.14.
 */
class ActionOnHotline implements MenuListItem.Action {
    @Override
    public void launch(Context context) {
        Caller caller = new Caller(context);
        caller.call(HotlineNumberProvider.get());
    }
}
