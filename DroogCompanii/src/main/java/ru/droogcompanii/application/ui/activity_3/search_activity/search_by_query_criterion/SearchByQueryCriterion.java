package ru.droogcompanii.application.ui.activity_3.search_activity.search_by_query_criterion;

import java.io.Serializable;

import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;

/**
 * Created by ls on 28.01.14.
 */
public class SearchByQueryCriterion implements SearchCriterion<String>, Serializable {

    private final String query;

    public SearchByQueryCriterion(String query) {
        this.query = query.trim();
    }

    @Override
    public boolean meetCriterion(String text) {
        return textMatchQuery(text);
    }

    private boolean textMatchQuery(String text) {
        return textMatchQueryInsensitive(text);
    }

    private boolean textMatchQueryInsensitive(String text) {
        String textInLowerCase = text.toLowerCase();
        String queryInLowerCase = query.toLowerCase();
        return textMatchQuerySensitive(textInLowerCase, queryInLowerCase);
    }

    private static boolean textMatchQuerySensitive(String text, String query) {
        String[] splittedQuery = splitIntoWords(query);
        if (noWords(splittedQuery)) {
            return true;
        }
        return textContainsWords(text, splittedQuery);
    }

    private static String[] splitIntoWords(String str) {
        return str.split("\\s+");
    }

    private static boolean noWords(String[] splittedQuery) {
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
