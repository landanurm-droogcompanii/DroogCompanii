package ru.droogcompanii.application.data.db_util;

/**
 * Created by ls on 07.03.14.
 */
public class DBUtils {
    public static String combineConditions(String combineOperator, String... conditions) {
        final int numberOfConditions = conditions.length;
        if (numberOfConditions == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(conditions[0]);
        for (int i = 1; i < numberOfConditions; ++i) {
            builder.append(" " + combineOperator + " " + conditions[i]);
        }
        return builder.toString();
    }
}
