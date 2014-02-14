package ru.droogcompanii.application.ui.activity.menu.menu_items_provider;

import android.content.Context;

import ru.droogcompanii.application.ui.activity.menu.MenuListItem;
import ru.droogcompanii.application.ui.activity.offer_list.OfferListActivity;

/**
 * Created by ls on 31.01.14.
 */
class ActionOnOffers implements MenuListItem.Action {
    @Override
    public void launch(Context context) {
        OfferListActivity.start(context);
    }
}
