package ru.droogcompanii.application.ui.fragment.filter_fragment.standard_filters.filter_impl;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter_fragment.standard_filters.search_criteria_and_comparators.ComparatorByDiscount;

/**
 * Created by ls on 21.01.14.
 */
class SortingFilterByDiscount extends FilterWithOneCheckbox {
    @Override
    protected boolean isNeedToIncludeByDefault() {
        return false;
    }

    @Override
    protected int getIdOfCheckbox() {
        return R.id.sortByDiscountCheckBox;
    }

    @Override
    protected void necessarilyIncludeIn(FilterSet filterSet) {
        filterSet.add(new ComparatorByDiscount());
    }
}