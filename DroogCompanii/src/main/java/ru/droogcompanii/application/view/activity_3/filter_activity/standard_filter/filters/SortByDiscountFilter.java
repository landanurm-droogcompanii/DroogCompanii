package ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.filters;


import java.io.Serializable;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnersReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.filter.SortingFilter;

/**
 * Created by ls on 16.01.14.
 */

public class SortByDiscountFilter extends SortingFilter<PartnerPoint> implements Serializable {
    @Override
    public int compare(PartnerPoint partnerPoint1, PartnerPoint partnerPoint2) {
        Integer discount1 = discountOf(partnerPoint1);
        Integer discount2 = discountOf(partnerPoint2);
        return discount1.compareTo(discount2);
    }

    private static int discountOf(PartnerPoint partnerPoint) {
        PartnersReader reader = new PartnersReader(DroogCompaniiApplication.getContext());
        Partner partner = reader.getPartnerOf(partnerPoint);
        return partner.discount;
    }
}
