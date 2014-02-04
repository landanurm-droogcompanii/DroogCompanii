package ru.droogcompanii.application.ui.activity.menu_activity.menu_items_provider;

import android.content.Context;

import ru.droogcompanii.application.ui.activity.menu_activity.MenuListItem;
import ru.droogcompanii.application.ui.activity.search_activity.SearchActivity;

/**
 * Created by ls on 31.01.14.
 */
class ActionOnPartnerCategories implements MenuListItem.Action {
    @Override
    public void launch(Context context) {
        SearchActivity.start(context, SearchActivity.UsageType.CATEGORIES);
    }
}
