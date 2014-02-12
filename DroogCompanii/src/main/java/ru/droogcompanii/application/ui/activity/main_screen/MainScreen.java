package ru.droogcompanii.application.ui.activity.main_screen;

import ru.droogcompanii.application.ui.activity.base_with_partner_points_map_fragment_and_info_panel.BaseActivityWithPartnerPointsMapFragmentAndInfoPanel;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsProvider;

/**
 * Created by ls on 14.01.14.
 */
public class MainScreen extends BaseActivityWithPartnerPointsMapFragmentAndInfoPanel {

    @Override
    protected PartnerPointsProvider getPartnerPointsProvider() {
        return new ClosePartnerPointsProvider();
    }

    @Override
    protected boolean isUpButtonEnabled() {
        return false;
    }
}
