package ru.droogcompanii.application.ui.fragment.filter.standard.filter_impl;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.fragment.filter.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner.PartnerComparatorByDiscount;
import ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner_point.PartnerPointComparatorByDiscount;

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
        filterSet.addPartnerPointComparator(new PartnerPointComparatorByDiscount());
        filterSet.addPartnerComparator(new PartnerComparatorByDiscount());
    }
}
