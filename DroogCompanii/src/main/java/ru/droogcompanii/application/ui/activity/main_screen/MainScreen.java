package ru.droogcompanii.application.ui.activity.main_screen;

import ru.droogcompanii.application.ui.activity.base_menu_helper.MenuHelper;
import ru.droogcompanii.application.ui.activity.base_menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper.MenuItemHelpers;
import ru.droogcompanii.application.ui.activity.base_with_partner_points_map_fragment_and_info_panel.BaseActivityWithPartnerPointsMapFragmentAndInfoPanel;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsProvider;

/**
 * Created by ls on 14.01.14.
 */
public class MainScreen extends BaseActivityWithPartnerPointsMapFragmentAndInfoPanel {

    @Override
    protected PartnerPointsProvider getPartnerPointsProvider() {
        return new AllPartnerPointsProvider();
    }

    @Override
    protected boolean isUpButtonEnabled() {
        return false;
    }

    @Override
    protected MenuHelper getMenuHelper() {
        return new MenuHelperItemsProvider(this) {
            @Override
            protected MenuItemHelper[] getMenuItemHelpers() {
                return prepareMenuItemHelpers();
            }
        };
    }

    private static MenuItemHelper[] prepareMenuItemHelpers() {
        return new MenuItemHelper[] {
                MenuItemHelpers.SEARCH,
                MenuItemHelpers.FILTER,
                MenuItemHelpers.OFFERS,
                MenuItemHelpers.PERSONAL_ACCOUNT,
                MenuItemHelpers.SYNCHRONIZATION,
                MenuItemHelpers.SETTINGS,
                MenuItemHelpers.HELP
        };
    }
}
