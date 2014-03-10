package ru.droogcompanii.application.ui.fragment.offer_list;

import com.google.common.base.Optional;

import ru.droogcompanii.application.data.db_util.ConditionsCombiner;
import ru.droogcompanii.application.data.db_util.offers.OffersContract;
import ru.droogcompanii.application.data.db_util.offers.SpecialOffersDBUtils;
import ru.droogcompanii.application.util.CalendarUtils;

/**
 * Created by Leonid on 08.03.14.
 */

class OffersConditionsPreparer {

    private static final ConditionsCombiner CONDITIONS_COMBINER = new ConditionsCombiner("AND");

    public static Optional<String> prepare(OfferType offerType, Optional<String> where) {
        Optional<String> offerTypeCondition = getConditionByOfferType(offerType);
        if (where.isPresent()) {
            String condition = CONDITIONS_COMBINER.combine(where.or(""), offerTypeCondition.or(""));
            return Optional.of(condition);
        } else {
            return offerTypeCondition;
        }
    }

    private static Optional<String> getConditionByOfferType(OfferType offerType) {
        switch (offerType) {
            case ALL:
                return Optional.absent();

            case SPECIAL:
                return Optional.of(prepareConditionForSpecialOffers());

            case ACTUAL:
                return Optional.of(prepareConditionForActualOffers());

            case PAST:
                return Optional.of(prepareConditionForPastOffers());

            default:
                throw new IllegalArgumentException("Unknown offer type: " + offerType);
        }
    }

    private static String prepareConditionForSpecialOffers() {
        return OffersContract.COLUMN_NAME_FROM + " = " + SpecialOffersDBUtils.getFrom() +
                " AND " + OffersContract.COLUMN_NAME_TO + " = " + SpecialOffersDBUtils.getTo();
    }

    private static String prepareConditionForActualOffers() {
        long now = CalendarUtils.now().getTimeInMillis();
        return SpecialOffersDBUtils.getTo() + " != " + OffersContract.COLUMN_NAME_TO + " AND " +
                now + " <= " + OffersContract.COLUMN_NAME_TO;
    }

    private static String prepareConditionForPastOffers() {
        long now = CalendarUtils.now().getTimeInMillis();
        return SpecialOffersDBUtils.getTo() + " != " + OffersContract.COLUMN_NAME_TO + " AND " +
                now + " > " + OffersContract.COLUMN_NAME_TO;
    }
}
