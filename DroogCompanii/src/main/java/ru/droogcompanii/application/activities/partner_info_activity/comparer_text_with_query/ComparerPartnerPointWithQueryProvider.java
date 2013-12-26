package ru.droogcompanii.application.activities.partner_info_activity.comparer_text_with_query;

/**
 * Created by ls on 25.12.13.
 */
public class ComparerPartnerPointWithQueryProvider {
    private static final ComparerPartnerPointWithQuery
            COMPARER_PARTNER_POINT_WITH_QUERY = new ComparerPartnerPointWithQueryImpl();

    public static ComparerPartnerPointWithQuery get() {
        return COMPARER_PARTNER_POINT_WITH_QUERY;
    }
}
