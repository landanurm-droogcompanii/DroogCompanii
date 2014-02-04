package ru.droogcompanii.application.ui.activity.main_screen;

import ru.droogcompanii.application.ui.activity.activity_with_partner_points_map_fragment_and_info_panel.ActivityWithPartnerPointsMapFragmentAndInfoPanel;
import ru.droogcompanii.application.ui.fragment.partner_points_map_fragment.PartnerPointsProvider;

/**
 * Created by ls on 14.01.14.
 */
public class MainScreen extends ActivityWithPartnerPointsMapFragmentAndInfoPanel {

    @Override
    protected PartnerPointsProvider getPartnerPointsProvider() {
        return new ClosePartnerPointsProvider();
    }

    @Override
    protected boolean isUpButtonEnabled() {
        return false;
    }
}
