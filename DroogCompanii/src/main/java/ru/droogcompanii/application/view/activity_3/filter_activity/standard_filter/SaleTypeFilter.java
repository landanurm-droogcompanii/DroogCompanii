package ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.data.data_structure.Partner;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnersReader;
import ru.droogcompanii.application.util.DroogCompaniiStringConstants;
import ru.droogcompanii.application.util.MoreComparableString;
import ru.droogcompanii.application.view.activity_3.filter_activity.filter.SearchFilter;

/**
 * Created by ls on 16.01.14.
 */

class SaleTypeFilter extends SearchFilter<PartnerPoint> implements Serializable {

    private List<String> saleTypes;

    SaleTypeFilter(boolean saleTypeBonus, boolean saleTypeDiscount, boolean saleTypeCashBack) {
        saleTypes = new ArrayList<String>();
        if (saleTypeBonus) {
            saleTypes.add(DroogCompaniiStringConstants.saleType_Bonus);
        }
        if (saleTypeDiscount) {
            saleTypes.add(DroogCompaniiStringConstants.saleType_Discount);
        }
        if (saleTypeCashBack) {
            saleTypes.add(DroogCompaniiStringConstants.saleType_CashBack);
        }
    }

    @Override
    public boolean meetCriteria(PartnerPoint partnerPoint) {
        MoreComparableString partnerPointSaleType = new MoreComparableString(saleTypeOf(partnerPoint));
        for (String saleType : saleTypes) {
            if (partnerPointSaleType.containsIgnoreCase(saleType)) {
                return true;
            }
        }
        return false;
    }

    private String saleTypeOf(PartnerPoint partnerPoint) {
        PartnersReader partnersReader = new PartnersReader(DroogCompaniiApplication.getContext());
        Partner partner = partnersReader.getPartnerOf(partnerPoint);
        return partner.saleType;
    }
}