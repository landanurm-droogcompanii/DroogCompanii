package ru.droogcompanii.application.ui.fragment.filter.standard.filter_impl;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.fragment.filter.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner.PartnerSearchCriterionByCashlessPayments;
import ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner_point.PartnerPointSearchCriterionByCashlessPayments;

/**
 * Created by ls on 21.01.14.
 */
class SearchByCashlessPaymentsFilter extends FilterWithOneCheckbox {
    @Override
    protected boolean isNeedToIncludeByDefault() {
        return false;
    }

    @Override
    protected int getIdOfCheckbox() {
        return R.id.cashlessPaymentsCheckBox;
    }

    @Override
    protected void necessarilyIncludeIn(FilterSet filterSet) {
        filterSet.addPartnerPointSearchCriterion(new PartnerPointSearchCriterionByCashlessPayments());
        filterSet.addPartnerSearchCriterion(new PartnerSearchCriterionByCashlessPayments());
    }

}
