package ru.droogcompanii.application.ui.activity.search_result_map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;

import ru.droogcompanii.application.ui.activity.menu_helper.MenuHelper;
import ru.droogcompanii.application.ui.activity.menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.ui.activity.menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.ui.activity.menu_helper.menu_item_helper.MenuItemHelpers;
import ru.droogcompanii.application.ui.activity.base_with_partner_points_map_fragment_and_info_panel.BaseActivityWithPartnerPointsMapFragmentAndInfoPanel;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsProvider;

/**
 * Created by ls on 14.01.14.
 */
public class SearchResultMapActivity extends BaseActivityWithPartnerPointsMapFragmentAndInfoPanel {

    private static final String KEY_PARTNER_POINTS_PROVIDER = "KEY_PARTNER_POINTS_PROVIDER";

    public static void start(Context context, PartnerPointsProvider partnerPointsProvider) {
        Intent intent = new Intent(context, SearchResultMapActivity.class);
        intent.putExtra(KEY_PARTNER_POINTS_PROVIDER, (Serializable) partnerPointsProvider);
        context.startActivity(intent);
    }

    @Override
    protected PartnerPointsProvider getPartnerPointsProvider() {
        Bundle args = getIntent().getExtras();
        return (PartnerPointsProvider) args.getSerializable(KEY_PARTNER_POINTS_PROVIDER);
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
