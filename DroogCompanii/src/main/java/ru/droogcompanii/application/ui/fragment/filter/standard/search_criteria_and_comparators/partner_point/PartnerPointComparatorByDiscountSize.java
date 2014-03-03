package ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner_point;


import java.io.Serializable;
import java.util.Comparator;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner.PartnerComparatorByDiscountSize;

/**
 * Created by ls on 16.01.14.
 */

public class PartnerPointComparatorByDiscountSize implements Comparator<PartnerPoint>, Serializable {

    @Override
    public int compare(PartnerPoint partnerPoint1, PartnerPoint partnerPoint2) {
        if (partnerPoint1.getPartnerId() == partnerPoint2.getPartnerId()) {
            return 0;
        }
        Comparator<Partner> comparator = new PartnerComparatorByDiscountSize();
        return comparator.compare(
                PartnersProviderWithoutContext.getPartnerOf(partnerPoint1),
                PartnersProviderWithoutContext.getPartnerOf(partnerPoint2)
        );
    }
}
