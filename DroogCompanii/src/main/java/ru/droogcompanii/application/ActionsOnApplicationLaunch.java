package ru.droogcompanii.application;

import ru.droogcompanii.application.global_flags.FlagNeedToUpdateMap;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterUtils;

/**
 * Created by ls on 07.02.14.
 */
public class ActionsOnApplicationLaunch {

    public static void actionsOnApplicationLaunch() {
        FilterUtils.resetFilters(DroogCompaniiApplication.getContext());
        FlagNeedToUpdateMap.init();
    }
}
