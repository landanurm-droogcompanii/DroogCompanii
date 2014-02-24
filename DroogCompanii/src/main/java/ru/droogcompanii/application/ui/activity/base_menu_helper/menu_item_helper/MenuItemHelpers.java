package ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper;

import android.support.v4.view.MenuItemCompat;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 14.02.14.
 */
public class MenuItemHelpers {
    public static final MenuItemHelper SEARCH = new MenuItemHelper()
            .withId(MenuItemIds.SEARCH).withOrder(MenuItemIds.SEARCH)
            .withTitleId(R.string.action_search)
            .withIcon(android.R.drawable.ic_menu_search)
            .withShowAsAction(MenuItemCompat.SHOW_AS_ACTION_IF_ROOM)
            .withAction(new ActionOnSearch());

    public static final MenuItemHelper FILTER = new MenuItemHelper()
            .withId(MenuItemIds.FILTER).withOrder(MenuItemIds.FILTER)
            .withTitleId(R.string.action_filter)
            .withIcon(R.drawable.ic_filter)
            .withShowAsAction(MenuItemCompat.SHOW_AS_ACTION_IF_ROOM)
            .withAction(new ActionOnFilter());

    public static final MenuItemHelper GO_TO_MAP = new MenuItemHelper()
            .withId(MenuItemIds.GO_TO_MAP).withOrder(MenuItemIds.GO_TO_MAP)
            .withTitleId(R.string.action_go_to_map)
            .withIcon(android.R.drawable.ic_dialog_map)
            .withShowAsAction(MenuItemCompat.SHOW_AS_ACTION_IF_ROOM)
            .withAction(new ActionOnGoToMap());

    public static final MenuItemHelper OFFERS = new MenuItemHelper()
            .withId(MenuItemIds.OFFERS).withOrder(MenuItemIds.OFFERS)
            .withTitleId(R.string.menu_item_offers)
            .withIcon(R.drawable.ic_offers)
            .withShowAsAction(MenuItemCompat.SHOW_AS_ACTION_IF_ROOM)
            .withAction(new ActionOnOffers());

    public static final MenuItemHelper LOGOUT = new MenuItemHelper()
            .withId(MenuItemIds.LOGOUT).withOrder(MenuItemIds.LOGOUT)
            .withTitleId(R.string.menu_item_logout)
            .withIcon(R.drawable.ic_logout)
            .withShowAsAction(MenuItemCompat.SHOW_AS_ACTION_ALWAYS)
            .withAction(new DummyAction());

    public static final MenuItemHelper PERSONAL_ACCOUNT = new MenuItemHelper()
            .withId(MenuItemIds.PERSONAL_ACCOUNT).withOrder(MenuItemIds.PERSONAL_ACCOUNT)
            .withTitleId(R.string.menu_item_personal_account)
            .withIcon(R.drawable.ic_personal_account)
            .withShowAsAction(MenuItemCompat.SHOW_AS_ACTION_NEVER)
            .withAction(new ActionOnPersonalAccount());

    public static final MenuItemHelper SYNCHRONIZATION = new MenuItemHelper()
            .withId(MenuItemIds.SYNCHRONIZATION).withOrder(MenuItemIds.SYNCHRONIZATION)
            .withTitleId(R.string.menu_item_synchronization)
            .withIcon(android.R.drawable.ic_popup_sync)
            .withShowAsAction(MenuItemCompat.SHOW_AS_ACTION_NEVER)
            .withAction(new ActionOnSynchronization());

    public static final MenuItemHelper SETTINGS = new MenuItemHelper()
            .withId(MenuItemIds.SETTINGS).withOrder(MenuItemIds.SETTINGS)
            .withTitleId(R.string.menu_item_settings)
            .withIcon(R.drawable.ic_action_settings)
            .withShowAsAction(MenuItemCompat.SHOW_AS_ACTION_NEVER)
            .withAction(new ActionOnSettings());

    public static final MenuItemHelper HELP = new MenuItemHelper()
            .withId(MenuItemIds.HELP).withOrder(MenuItemIds.HELP)
            .withTitleId(R.string.menu_item_help)
            .withIcon(R.drawable.ic_action_help)
            .withShowAsAction(MenuItemCompat.SHOW_AS_ACTION_NEVER)
            .withAction(new ActionOnHelp());
}
