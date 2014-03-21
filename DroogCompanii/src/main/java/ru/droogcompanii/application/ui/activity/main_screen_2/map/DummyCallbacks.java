package ru.droogcompanii.application.ui.activity.main_screen_2.map;

import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by ls on 21.03.14.
 */
class DummyCallbacks implements NewPartnerPointsMapFragment.Callbacks {
    @Override
    public void onDisplayDetails(List<PartnerPoint> partnerPoints) {
        LogUtils.debug("DUMMY_CALLBACKS: onDisplayDetails()");
    }

    @Override
    public void onHideDetails() {
        LogUtils.debug("DUMMY_CALLBACKS: onHideDetails()");
    }
}
