package ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper;

import android.app.Activity;

import ru.droogcompanii.application.ui.activity.offer_list.OfferListActivity;

/**
 * Created by ls on 14.02.14.
 */
class ActionOnOffers implements MenuItemHelper.Action {
    @Override
    public void run(Activity activity) {
        OfferListActivity.start(activity);
    }
}
