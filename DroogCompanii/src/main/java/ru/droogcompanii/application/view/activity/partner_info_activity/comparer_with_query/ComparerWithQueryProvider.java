package ru.droogcompanii.application.view.activity.partner_info_activity.comparer_with_query;

/**
 * Created by ls on 25.12.13.
 */
public class ComparerWithQueryProvider {
    private static final ComparerWithQuery COMPARER_WITH_QUERY = new ComparerWithQueryImpl();

    public static ComparerWithQuery get() {
        return COMPARER_WITH_QUERY;
    }
}
