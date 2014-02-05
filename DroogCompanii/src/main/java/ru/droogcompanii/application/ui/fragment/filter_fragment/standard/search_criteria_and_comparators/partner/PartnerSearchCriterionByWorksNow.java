package ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators.partner;

import ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators.NowCalendarProvider;

/**
 * Created by ls on 16.01.14.
 */
public class PartnerSearchCriterionByWorksNow extends PartnerSearchCriterionByWorksAtSomeTime {

    public PartnerSearchCriterionByWorksNow() {
        super(new NowCalendarProvider());
    }
}
