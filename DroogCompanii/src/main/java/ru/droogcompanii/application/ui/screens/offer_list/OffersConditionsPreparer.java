package ru.droogcompanii.application.ui.screens.offer_list;

import com.google.common.base.Optional;

import ru.droogcompanii.application.data.db_util.ConditionsCombiner;
import ru.droogcompanii.application.data.db_util.contracts.OffersContract;
import ru.droogcompanii.application.data.db_util.offers.SpecialOffersDBUtils;
import ru.droogcompanii.application.util.CalendarUtils;

/**
 * Created by Leonid on 08.03.14.
 */

class OffersConditionsPreparer {

    private static final ConditionsCombiner CONDITIONS_COMBINER = new ConditionsCombiner("AND");

    public static Optional<String> prepare(OfferType offerType, Optional<String> condition) {
        if (offerType.equals(OfferType.ALL)) {
            return condition;
        }
        String resultCondition = getConditionByOfferType(offerType);
        if (condition.isPresent()) {
            resultCondition = CONDITIONS_COMBINER.combine(condition.get(), resultCondition);
        }
        return Optional.of(resultCondition);
    }

    private static String getConditionByOfferType(OfferType offerType) {
        switch (offerType) {
            case SPECIAL:
                return prepareConditionForSpecialOffers();

            case ACTUAL:
                return prepareConditionForActualOffers();

            case PAST:
                return prepareConditionForPastOffers();

            default:
                throw new IllegalArgumentException("Unknown offer type: " + offerType);
        }
    }

    private static String prepareConditionForSpecialOffers() {
        return CONDITIONS_COMBINER.combine(
                OffersContract.COLUMN_FROM + " = " + SpecialOffersDBUtils.getFrom(),
                OffersContract.COLUMN_TO + " = " + SpecialOffersDBUtils.getTo()
        );
    }

    private static String prepareConditionForActualOffers() {
        long now = CalendarUtils.now().getTimeInMillis();
        return CONDITIONS_COMBINER.combine(
                SpecialOffersDBUtils.getTo() + " != " + OffersContract.COLUMN_TO,
                now + " <= " + OffersContract.COLUMN_TO
        );
    }

    private static String prepareConditionForPastOffers() {
        long now = CalendarUtils.now().getTimeInMillis();
        return CONDITIONS_COMBINER.combine(
                SpecialOffersDBUtils.getTo() + " != " + OffersContract.COLUMN_TO,
                now + " > " + OffersContract.COLUMN_TO
        );
    }
}
