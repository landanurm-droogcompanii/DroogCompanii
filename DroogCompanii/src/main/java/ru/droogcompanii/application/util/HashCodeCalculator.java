package ru.droogcompanii.application.util;

/**
 * Created by Leonid on 09.02.14.
 */
public class HashCodeCalculator {
    public static int hashCodeFromFields(Object... fields) {
        int hashCode = 0;
        for (Object each : fields) {
            hashCode = 31 * hashCode + hashCodeFrom(each);
        }
        return hashCode;
    }

    private static int hashCodeFrom(Object obj) {
        return obj == null ? 0 : obj.hashCode();
    }
}
