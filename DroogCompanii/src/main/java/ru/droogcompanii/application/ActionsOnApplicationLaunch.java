package ru.droogcompanii.application;

import ru.droogcompanii.application.ui.fragment.filter.FilterUtils;
import ru.droogcompanii.application.ui.util.location.CustomBaseLocationUtils;
import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by ls on 07.02.14.
 */
public class ActionsOnApplicationLaunch {

    public static void actionsOnApplicationLaunch() {
        FilterUtils.resetFilters(DroogCompaniiApplication.getContext());
        CustomBaseLocationUtils.init();
    }

    private static void handleUnhandledException() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                LogUtils.exception(throwable);
            }
        });
    }

}
