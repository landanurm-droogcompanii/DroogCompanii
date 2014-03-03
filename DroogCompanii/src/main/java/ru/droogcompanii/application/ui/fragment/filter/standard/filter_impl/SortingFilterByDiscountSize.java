package ru.droogcompanii.application.ui.fragment.filter.standard.filter_impl;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.fragment.filter.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner.PartnerComparatorByDiscountSize;
import ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner_point.PartnerPointComparatorByDiscountSize;

/**
 * Created by ls on 21.01.14.
 */
class SortingFilterByDiscountSize extends FilterWithOneCheckbox {

    @Override
    protected boolean isNeedToIncludeByDefault() {
        return false;
    }

    @Override
    protected int getIdOfCheckbox() {
        return R.id.sortByDiscountSizeCheckBox;
    }

    @Override
    protected void necessarilyIncludeIn(FilterSet filterSet) {
        filterSet.addPartnerPointComparator(new PartnerPointComparatorByDiscountSize());
        filterSet.addPartnerComparator(new PartnerComparatorByDiscountSize());
    }

}
