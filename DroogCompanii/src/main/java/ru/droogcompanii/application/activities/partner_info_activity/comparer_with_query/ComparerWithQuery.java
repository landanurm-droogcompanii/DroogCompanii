package ru.droogcompanii.application.activities.partner_info_activity.comparer_with_query;

import ru.droogcompanii.application.data.data_structure.PartnerPoint;

/**
 * Created by ls on 25.12.13.
 */
public interface ComparerWithQuery {
    boolean partnerPointMatchQuery(PartnerPoint partnerPoint, String query);
}
