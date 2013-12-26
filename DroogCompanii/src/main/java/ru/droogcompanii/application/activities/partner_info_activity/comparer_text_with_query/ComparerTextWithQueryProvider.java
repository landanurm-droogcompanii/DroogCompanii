package ru.droogcompanii.application.activities.partner_info_activity.comparer_text_with_query;

/**
 * Created by ls on 25.12.13.
 */
public class ComparerTextWithQueryProvider {
    private static final ComparerTextWithQuery comparerTextWithQuery = new ComparerTextWithQueryImpl();

    public static ComparerTextWithQuery get() {
        return comparerTextWithQuery;
    }
}
