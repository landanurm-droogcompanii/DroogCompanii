package ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.filters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnersReader;
import ru.droogcompanii.application.util.DroogCompaniiStringConstants;
import ru.droogcompanii.application.util.MoreComparableString;
import ru.droogcompanii.application.data.searchable_sortable_listing.filter.SearchFilter;
import ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.state.StandardFiltersState;

/**
 * Created by ls on 16.01.14.
 */

public class DiscountTypeFilter extends SearchFilter<PartnerPoint> implements Serializable {

    private List<String> discountTypes;

    public DiscountTypeFilter(StandardFiltersState state) {
        discountTypes = new ArrayList<String>();
        if (state.discountTypeBonus) {
            discountTypes.add(DroogCompaniiStringConstants.discountType_Bonus);
        }
        if (state.discountTypeDiscount) {
            discountTypes.add(DroogCompaniiStringConstants.discountType_Discount);
        }
        if (state.discountTypeCashBack) {
            discountTypes.add(DroogCompaniiStringConstants.discountType_CashBack);
        }

    }

    @Override
    public boolean meetCriterion(PartnerPoint partnerPoint) {
        MoreComparableString partnerPointDiscountType = new MoreComparableString(discountTypeOf(partnerPoint));
        for (String discountType : discountTypes) {
            if (partnerPointDiscountType.containsIgnoreCase(discountType)) {
                return true;
            }
        }
        return false;
    }

    private String discountTypeOf(PartnerPoint partnerPoint) {
        PartnersReader partnersReader = new PartnersReader(DroogCompaniiApplication.getContext());
        Partner partner = partnersReader.getPartnerOf(partnerPoint);
        return partner.discountType;
    }
}