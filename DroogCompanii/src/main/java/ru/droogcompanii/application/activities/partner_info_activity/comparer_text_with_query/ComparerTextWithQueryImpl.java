package ru.droogcompanii.application.activities.partner_info_activity.comparer_text_with_query;

/**
 * Created by ls on 25.12.13.
 */
class ComparerTextWithQueryImpl implements ComparerTextWithQuery {

    @Override
    public boolean textMatchQuery(String text, String query) {
        return textMatchQueryInsensitive(text, query);
    }

    private static boolean textMatchQueryInsensitive(String text, String query) {
        String textInLowerCase = text.toLowerCase();
        String queryInLowerCase = query.toLowerCase();
        return textMatchQuerySensitive(textInLowerCase, queryInLowerCase);
    }

    private static boolean textMatchQuerySensitive(String text, String query) {
        String[] splittedQuery = splitStringOnMultipleWhitespacesIntoWords(query);
        if (queryContainsOnlyWhitespaces(splittedQuery)) {
            return true;
        }
        return textContainsWords(text, splittedQuery);
    }

    private static String[] splitStringOnMultipleWhitespacesIntoWords(String str) {
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
