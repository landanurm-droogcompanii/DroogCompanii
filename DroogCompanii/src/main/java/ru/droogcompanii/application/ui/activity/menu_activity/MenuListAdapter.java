package ru.droogcompanii.application.ui.activity.menu_activity;

import android.content.Context;
import android.content.res.Resources;

import ru.droogcompanii.application.ui.activity.menu_activity.menu_items_provider.MenuItemsProvider;
import ru.droogcompanii.application.ui.helpers.SimpleArrayAdapter;

/**
 * Created by ls on 31.01.14.
 */

public class MenuListAdapter extends SimpleArrayAdapter<MenuListItem> {


    public MenuListAdapter(final Context context) {
        super(context, MenuItemsProvider.getMenuItems(), new ItemToTitleConvertor<MenuListItem>() {
            @Override
            public String getTitle(MenuListItem item) {
                Resources resources = context.getResources();
                String title = resources.getString(item.titleId);
                return title;
            }
        });
    }
}
