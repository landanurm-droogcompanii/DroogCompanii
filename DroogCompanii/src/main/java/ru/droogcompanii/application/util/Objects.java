package ru.droogcompanii.application.util;

/**
 * Created by ls on 11.02.14.
 */
public class Objects {

    public static boolean equals(Object obj1, Object obj2) {
        return (obj1 == null) ? (obj2 == null) : obj1.equals(obj2);
    }

    public static int hash(Object... fields) {
        int hashCode = 0;
        for (Object each : fields) {
            hashCode = 31 * hashCode + hashCode(each);
        }
        return hashCode;
    }

    public static int hashCode(Object obj) {
        return obj == null ? 0 : obj.hashCode();
    }
}
