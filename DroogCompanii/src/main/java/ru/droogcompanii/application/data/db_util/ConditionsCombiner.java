package ru.droogcompanii.application.data.db_util;

/**
 * Created by Leonid on 08.03.14.
 */
public class ConditionsCombiner {
    private final String combineOperator;

    public ConditionsCombiner(String combineOperator) {
        this.combineOperator = " " + combineOperator + " ";
    }

    public String combine(String... conditions) {
        final int numberOfConditions = conditions.length;
        if (numberOfConditions == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(conditions[0]);
        for (int i = 1; i < numberOfConditions; ++i) {
            builder.append(combineOperator);
            builder.append(conditions[i]);
        }
        return builder.toString();
    }
}
