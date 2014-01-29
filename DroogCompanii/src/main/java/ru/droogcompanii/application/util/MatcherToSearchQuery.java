package ru.droogcompanii.application.util;

import java.io.Serializable;

/**
 * Created by ls on 29.01.14.
 */
public class MatcherToSearchQuery implements Serializable {
    private final String query;

    public MatcherToSearchQuery(String query) {
        this.query = query.trim();
    }

    public boolean matchToQuery(String text) {
        return matchToQueryInsensitive(text);
    }

    private boolean matchToQueryInsensitive(String text) {
        String textInLowerCase = text.toLowerCase();
        String queryInLowerCase = query.toLowerCase();
        return matchToQuerySensitive(textInLowerCase, queryInLowerCase);
    }

    private static boolean matchToQuerySensitive(String text, String query) {
        String[] splittedQuery = splitIntoWords(query);
        if (noWords(splittedQuery)) {
            return true;
        }
        return textContainsWords(text, splittedQuery);
    }

    private static String[] splitIntoWords(String str) {
        return str.split("\\s+");
    }

    private static boolean noWords(String[] words) {
        return words.length == 0;
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
