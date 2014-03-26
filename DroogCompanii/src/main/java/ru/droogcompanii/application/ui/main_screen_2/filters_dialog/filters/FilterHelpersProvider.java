package ru.droogcompanii.application.ui.main_screen_2.filters_dialog.filters;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ls on 25.03.14.
 */
class FilterHelpersProvider {

    public static List<FilterHelper> get() {
        return Arrays.<FilterHelper>asList(
                new CashlessPaymentsFilterHelper(),
                new WorksNowFilterHelper(),
                new DistanceFilterHelper()
        );
    }
}
