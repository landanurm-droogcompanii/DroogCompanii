package ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.filters;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.view.fragment.filter_fragment.Filters;
import ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.search_criteria_and_comparators.ComparatorByDiscount;

/**
 * Created by ls on 21.01.14.
 */
public class FilterSortByDiscount extends FilterWithOneCheckbox {
    @Override
    protected boolean isNeedToIncludeByDefault() {
        return false;
    }

    @Override
    protected int getIdOfCheckbox() {
        return R.id.sortByDiscountCheckBox;
    }

    @Override
    protected void necessarilyIncludeIn(Filters filters) {
        filters.add(new ComparatorByDiscount());
    }
}
