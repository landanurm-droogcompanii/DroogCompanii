package ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter;

import android.content.SharedPreferences;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable.filter.Filter;
import ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.filters.CashlessPaymentsFilter;
import ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.filters.DiscountTypeFilter;
import ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.filters.SortByDiscountFilter;
import ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.filters.SortByDistanceBasedOnCurrentLocationFilter;
import ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.filters.WorksNowFilter;
import ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.state.StandardFiltersState;
import ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.state.StandardFiltersStateReader;

/**
 * Created by ls on 16.01.14.
 */
public class StandardFiltersReader {

    private final StandardFiltersStateReader standardFiltersStateReader;

    public StandardFiltersReader(View viewOfFilters) {
        standardFiltersStateReader = new StandardFiltersStateReader(viewOfFilters);
    }

    public StandardFiltersReader(SharedPreferences sharedPreferences) {
        standardFiltersStateReader = new StandardFiltersStateReader(sharedPreferences);
    }

    public List<Filter<PartnerPoint>> read() {
        List<Filter<PartnerPoint>> filters = new ArrayList<Filter<PartnerPoint>>();
        StandardFiltersState state = standardFiltersStateReader.read();
        if (state.sortByDistance) {
            filters.add(new SortByDistanceBasedOnCurrentLocationFilter());
        }
        if (state.sortByDiscount) {
            filters.add(new SortByDiscountFilter());
        }
        if (state.cashlessPayments) {
            filters.add(new CashlessPaymentsFilter());
        }
        if (state.worksNow) {
            filters.add(new WorksNowFilter());
        }
        DiscountTypeFilter discountTypeFilter = new DiscountTypeFilter(state);
        filters.add(discountTypeFilter);

        return filters;
    }
}