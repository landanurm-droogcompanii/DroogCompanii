package ru.droogcompanii.application.ui.main_screen_2.filters_dialog.filters;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ls on 25.03.14.
 */
public class FiltersProvider {

    public static List<Filter> getFilters() {
        return Arrays.<Filter>asList(
                new CashlessPaymentsFilter(),
                new WorksNowFilter(),
                new DistanceFilter()
        );
    }
}
