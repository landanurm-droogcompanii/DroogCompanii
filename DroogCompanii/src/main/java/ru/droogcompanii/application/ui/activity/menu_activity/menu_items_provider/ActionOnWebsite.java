package ru.droogcompanii.application.ui.activity.menu_activity.menu_items_provider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import ru.droogcompanii.application.ui.activity.menu_activity.MenuListItem;
import ru.droogcompanii.application.util.WebsiteAddressProvider;

/**
 * Created by ls on 31.01.14.
 */
class ActionOnWebsite implements MenuListItem.Action {
    @Override
    public void launch(Context context) {
        final String websiteAddress = WebsiteAddressProvider.getWebsiteAddress();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteAddress));
        context.startActivity(intent);
    }
}
