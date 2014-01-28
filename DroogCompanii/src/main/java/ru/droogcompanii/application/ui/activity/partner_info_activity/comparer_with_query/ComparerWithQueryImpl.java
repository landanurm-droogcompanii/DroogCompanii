package ru.droogcompanii.application.ui.activity.partner_info_activity.comparer_with_query;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 25.12.13.
 */
class ComparerWithQueryImpl implements ComparerWithQuery {

    @Override
    public boolean partnerPointMatchQuery(PartnerPoint partnerPoint, String query) {
        return textMatchQuery(partnerPoint.title, query);
    }

    private static boolean textMatchQuery(String text, String query) {
        return textMatchQueryInsensitive(text, query);
    }

    private static boolean textMatchQueryInsensitive(String text, String query) {
        String textInLowerCase = text.toLowerCase();
        String queryInLowerCase = query.toLowerCase();
        return textMatchQuerySensitive(textInLowerCase, queryInLowerCase);
    }

    private static boolean textMatchQuerySensitive(String text, String query) {
        String[] splittedQuery = splitIntoWords(query);
        if (queryContainsOnlyWhitespaces(splittedQuery)) {
            return true;
        }
        return textContainsWords(text, splittedQuery);
    }

    private static String[] splitIntoWords(String str) {
        return str.split("\\s+");
    }

    private static boolean queryContainsOnlyWhitespaces(String[] splittedQuery) {
        return splittedQuery.length == 0;
    }

    private static boolean textContainsWords(String text, String[] words) {
        for (String word : words) {
            if (!text.contains(word)) {
                return false;
            }
        }
        return true;
    }
}
