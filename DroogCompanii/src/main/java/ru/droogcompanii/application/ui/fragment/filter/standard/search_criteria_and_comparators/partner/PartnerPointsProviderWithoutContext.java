package ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner;

import android.content.Context;

import java.util.List;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerPointsReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 05.02.14.
 */
class PartnerPointsProviderWithoutContext {
    static List<PartnerPoint> getPointsOf(Partner partner) {
        PartnerPointsReader partnerPointsReader = new PartnerPointsReader(getApplicationContext());
        return partnerPointsReader.getPartnerPointsOf(partner);
    }

    private static Context getApplicationContext() {
        return DroogCompaniiApplication.getContext();
    }
}
