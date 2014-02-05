package ru.droogcompanii.application.ui.fragment.filter_fragment.standard.filter_impl;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators.partner.PartnerSearchCriterionByWorksNow;
import ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators.partner_point.PartnerPointSearchCriterionByWorksNow;

/**
 * Created by ls on 21.01.14.
 */
class SearchFilterByWorksNow extends FilterWithOneCheckbox {
    @Override
    protected boolean isNeedToIncludeByDefault() {
        return false;
    }

    @Override
    protected int getIdOfCheckbox() {
        return R.id.worksNowCheckBox;
    }

    @Override
    protected void necessarilyIncludeIn(FilterSet filterSet) {
        filterSet.addPartnerPointSearchCriterion(new PartnerPointSearchCriterionByWorksNow());
        filterSet.addPartnerSearchCriterion(new PartnerSearchCriterionByWorksNow());
    }
}
