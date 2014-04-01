package ru.droogcompanii.application.ui.screens.main;

import com.google.common.base.Optional;

import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerHierarchyContracts;

/**
 * Created by ls on 27.03.14.
 */
class ConditionConverter {

    public static final PartnerHierarchyContracts.PartnersContract
            PARTNERS = new PartnerHierarchyContracts.PartnersContract();

    public static final PartnerHierarchyContracts.PartnerPointsContract
            PARTNER_POINTS = new PartnerHierarchyContracts.PartnerPointsContract();


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
            return PARTNER_POINTS.COLUMN_PARTNER_ID + " IN (" +
                    " SELECT " + PARTNERS.COLUMN_ID +
                    " FROM " + PARTNERS.TABLE_NAME +
                    " WHERE " + conditionToReceivePartners +
            ")";
        }
    }

}
