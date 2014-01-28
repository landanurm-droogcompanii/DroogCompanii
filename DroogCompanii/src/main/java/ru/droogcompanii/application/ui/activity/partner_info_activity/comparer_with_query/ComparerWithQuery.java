package ru.droogcompanii.application.ui.activity.partner_info_activity.comparer_with_query;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 25.12.13.
 */
public interface ComparerWithQuery {
    boolean partnerPointMatchQuery(PartnerPoint partnerPoint, String query);
}
