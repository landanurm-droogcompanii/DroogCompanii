package ru.droogcompanii.application.ui.fragment.filter_fragment.standard.filter_impl;

import java.util.Arrays;
import java.util.List;

import ru.droogcompanii.application.ui.fragment.filter_fragment.Filter;

/**
 * Created by ls on 22.01.14.
 */
public class StandardFiltersProvider {
    public static List<Filter> getDefaultFilters() {
        final Filter[] standardFiltersInitializedByDefault = {
                new SearchFilterByCashlessPayments(),
                new SearchFilterByDiscountType(),
                new SearchFilterByWorksNow(),
                new SortingFilterByDiscount(),
                new SortingFilterByDistanceBasedOnCurrentLocation(),
                new SortingFilterByTitle()
        };
        return Arrays.asList(standardFiltersInitializedByDefault);
    }
}
