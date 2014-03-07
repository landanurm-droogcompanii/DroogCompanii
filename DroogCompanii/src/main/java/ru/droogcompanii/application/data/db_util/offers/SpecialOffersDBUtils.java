package ru.droogcompanii.application.data.db_util.offers;

/**
 * Created by ls on 06.03.14.
 */
public class SpecialOffersDBUtils {

    public static long getFrom() {
        return 0L;
    }

    public static long getTo() {
        return 0L;
    }

    public static boolean isDurationOfSpecialOffers(long from, long to) {
        return (from == getFrom()) && (to == getTo());
    }
}
