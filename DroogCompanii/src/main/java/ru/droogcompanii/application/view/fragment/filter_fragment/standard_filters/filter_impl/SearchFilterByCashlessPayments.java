package ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.filter_impl;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.view.fragment.filter_fragment.filters.Filters;
import ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.search_criteria_and_comparators.SearchCriterionByCashlessPayments;

/**
 * Created by ls on 21.01.14.
 */
class SearchFilterByCashlessPayments extends FilterWithOneCheckbox {
    @Override
    protected boolean isNeedToIncludeByDefault() {
        return false;
    }

    @Override
    protected int getIdOfCheckbox() {
        return R.id.cashlessPaymentsCheckBox;
    }

    @Override
    protected void includeIn(Filters filters) {
        filters.add(new SearchCriterionByCashlessPayments());
    }
}
