package ru.droogcompanii.application;

import ru.droogcompanii.application.util.SharedPreferencesProvider;
import ru.droogcompanii.application.util.location.CustomBaseLocationUtils;

/**
 * Created by ls on 07.02.14.
 */
public class ActionsOnApplicationLaunch {

    public static void actionsOnApplicationLaunch() {
        SharedPreferencesProvider.clear(DroogCompaniiApplication.getContext());
        CustomBaseLocationUtils.init();
    }

}
