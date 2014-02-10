package ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner_point;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnersReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 05.02.14.
 */
class PartnersProviderWithoutContext {
    static Partner getPartnerOf(PartnerPoint partnerPoint) {
        PartnersReader partnersReader = new PartnersReader(DroogCompaniiApplication.getContext());
        return partnersReader.getPartnerOf(partnerPoint);
    }
}
