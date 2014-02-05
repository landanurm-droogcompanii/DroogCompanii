package ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators.partner;


import java.io.Serializable;
import java.util.Comparator;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;

/**
 * Created by ls on 16.01.14.
 */

public class PartnerComparatorByDiscount implements Comparator<Partner>, Serializable {
    @Override
    public int compare(Partner partner1, Partner partner2) {
        Integer discount1 = partner1.discount;
        Integer discount2 = partner2.discount;
        return discount1.compareTo(discount2);
    }
}
