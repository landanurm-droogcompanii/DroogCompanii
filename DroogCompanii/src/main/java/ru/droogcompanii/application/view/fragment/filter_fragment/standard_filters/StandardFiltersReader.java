package ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters;

import android.content.SharedPreferences;
import android.view.View;

import ru.droogcompanii.application.view.fragment.filter_fragment.Filters;
import ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.search_criteria_and_comparators.CashlessPaymentsSearchCriterion;
import ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.search_criteria_and_comparators.ComparatorByDiscount;
import ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.search_criteria_and_comparators.ComparatorByDistanceBasedOnCurrentLocation;
import ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.search_criteria_and_comparators.ComparatorByTitle;
import ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.search_criteria_and_comparators.DiscountTypeSearchCriterion;
import ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.search_criteria_and_comparators.WorksNowSearchCriterion;
import ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.state.StandardFiltersState;
import ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.state.StandardFiltersStateReader;

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

    public Filters read() {
        Filters filters = new Filters();
        StandardFiltersState state = standardFiltersStateReader.read();
        if (state.sortByTitle) {
            filters.add(new ComparatorByTitle());
        }
        if (state.sortByDistance) {
            filters.add(new ComparatorByDistanceBasedOnCurrentLocation());
        }
        if (state.sortByDiscount) {
            filters.add(new ComparatorByDiscount());
        }
        if (state.cashlessPayments) {
            filters.add(new CashlessPaymentsSearchCriterion());
        }
        if (state.worksNow) {
            filters.add(new WorksNowSearchCriterion());
        }
        DiscountTypeSearchCriterion discountTypeSearchCriterion = new DiscountTypeSearchCriterion(state);
        filters.add(discountTypeSearchCriterion);

        return filters;
    }
}