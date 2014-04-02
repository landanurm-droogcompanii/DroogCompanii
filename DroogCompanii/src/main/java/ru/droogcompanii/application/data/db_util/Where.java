package ru.droogcompanii.application.data.db_util;

/**
 * Created by ls on 02.04.14.
 */
public class Where {

    public static String byCondition(String condition) {
        if (condition == null) {
            throw new IllegalArgumentException();
        }
        if (condition.trim().isEmpty()) {
            return "";
        }
        return (" WHERE " + condition);
    }
}
