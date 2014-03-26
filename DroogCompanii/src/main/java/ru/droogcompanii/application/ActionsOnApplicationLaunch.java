package ru.droogcompanii.application;

import ru.droogcompanii.application.ui.fragment.filter.FilterUtils;
import ru.droogcompanii.application.util.location.CustomBaseLocationUtils;

/**
 * Created by ls on 07.02.14.
 */
public class ActionsOnApplicationLaunch {

    public static void actionsOnApplicationLaunch() {
        FilterUtils.resetFilters(DroogCompaniiApplication.getContext());
        CustomBaseLocationUtils.init();
    }

}
