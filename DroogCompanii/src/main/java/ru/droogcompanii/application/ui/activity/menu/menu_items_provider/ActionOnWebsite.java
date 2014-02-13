package ru.droogcompanii.application.ui.activity.menu.menu_items_provider;

import android.content.Context;

import ru.droogcompanii.application.ui.activity.menu.MenuListItem;
import ru.droogcompanii.application.util.NavigationToWebsiteHelper;
import ru.droogcompanii.application.util.WebsiteAddressProvider;

/**
 * Created by ls on 31.01.14.
 */
class ActionOnWebsite implements MenuListItem.Action {
    @Override
    public void launch(Context context) {
        NavigationToWebsiteHelper helper = new NavigationToWebsiteHelper(context);
        helper.navigateToSite(WebsiteAddressProvider.getWebsiteAddress());
    }
}
