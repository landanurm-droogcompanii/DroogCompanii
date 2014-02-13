package ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner;


import java.io.Serializable;
import java.util.Comparator;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;

/**
 * Created by ls on 16.01.14.
 */

public class PartnerComparatorByDiscountSize implements Comparator<Partner>, Serializable {
    @Override
    public int compare(Partner partner1, Partner partner2) {
        Integer discountSize1 = partner1.getDiscountSize();
        Integer discountSize2 = partner2.getDiscountSize();
        return discountSize1.compareTo(discountSize2);
    }
}
