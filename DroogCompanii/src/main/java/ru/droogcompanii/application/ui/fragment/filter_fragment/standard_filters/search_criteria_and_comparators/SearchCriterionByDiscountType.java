package ru.droogcompanii.application.ui.fragment.filter_fragment.standard_filters.search_criteria_and_comparators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnersReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.util.DroogCompaniiStringConstants;
import ru.droogcompanii.application.util.MoreComparableString;

/**
 * Created by ls on 16.01.14.
 */

public class SearchCriterionByDiscountType
        implements SearchCriterion<PartnerPoint>, Serializable {

    private List<String> discountTypes;

    public SearchCriterionByDiscountType(boolean bonus, boolean discount, boolean cashBack) {
        discountTypes = new ArrayList<String>();
        if (bonus) {
            discountTypes.add(DroogCompaniiStringConstants.discountType_Bonus);
        }
        if (discount) {
            discountTypes.add(DroogCompaniiStringConstants.discountType_Discount);
        }
        if (cashBack) {
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