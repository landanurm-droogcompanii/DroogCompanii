package ru.droogcompanii.application.ui.screens.partner_list.comparators;

import java.io.Serializable;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;

/**
 * Created by ls on 02.04.14.
 */
public class ComparatorByDiscountSize extends BaseComparator<Partner> implements Serializable {
    @Override
    public int compare(Partner partner1, Partner partner2) {
        Integer discountSize1 = partner1.getDiscountSize();
        Integer discountSize2 = partner2.getDiscountSize();
        return discountSize1.compareTo(discountSize2);
    }
}