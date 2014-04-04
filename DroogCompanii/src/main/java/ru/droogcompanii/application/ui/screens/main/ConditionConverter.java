package ru.droogcompanii.application.ui.screens.main;

import com.google.common.base.Optional;

import ru.droogcompanii.application.data.db_util.contracts.*;

/**
 * Created by ls on 27.03.14.
 */
class ConditionConverter {

    public static class ConditionToReceivePartnerPoints {

        public static Optional<String> from(Optional<String> optionalConditionToReceivePartners) {
            if (!optionalConditionToReceivePartners.isPresent()) {
                return Optional.absent();
            }
            String conditionToReceivePartners = optionalConditionToReceivePartners.get().trim();
            return Optional.of(convertToConditionToReceivePartnerPoints(conditionToReceivePartners));
        }

        private static String convertToConditionToReceivePartnerPoints(String conditionToReceivePartners) {
            if (conditionToReceivePartners.isEmpty()) {
                return conditionToReceivePartners;
            }
            return PartnerPointsContract.COLUMN_PARTNER_ID + " IN (" +
                    " SELECT " + PartnersContract.COLUMN_ID +
                    " FROM " + PartnersContract.TABLE_NAME +
                    " WHERE " + conditionToReceivePartners +
            ")";
        }
    }

}
