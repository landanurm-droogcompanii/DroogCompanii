package ru.droogcompanii.application.ui.activity.menu.menu_items_provider;

import java.util.Arrays;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.activity.menu.MenuListItem;

/**
 * Created by ls on 31.01.14.
 */
public class MenuItemsProvider {

    private static final MenuListItem[] MENU_LIST_ITEMS;

    static {
        MENU_LIST_ITEMS = new MenuListItem[] {
                new MenuListItem(R.string.menu_item_personal_account, new ActionOnPersonalAccount()),
                new MenuListItem(R.string.menu_item_partner_categories, new ActionOnPartnerCategories()),
                new MenuListItem(R.string.menu_item_offers, new ActionOnOffers()),
                new MenuListItem(R.string.menu_item_website, new ActionOnWebsite()),
                new MenuListItem(R.string.hotline, new ActionOnHotline()),
                new MenuListItem(R.string.menu_item_synchronization, new ActionOnSynchronization()),
                new MenuListItem(R.string.menu_item_settings, new ActionOnSettings())
        };
    }

    public static List<MenuListItem> getMenuItems() {
        return Arrays.asList(MENU_LIST_ITEMS);
    }
}
