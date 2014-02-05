package ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators.partner_point;

import ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators.NowCalendarProvider;

/**
 * Created by ls on 16.01.14.
 */
public class PartnerPointSearchCriterionByWorksNow extends PartnerPointSearchCriterionByWorksAtSomeTime {

    public PartnerPointSearchCriterionByWorksNow() {
        super(new NowCalendarProvider());
    }
}
