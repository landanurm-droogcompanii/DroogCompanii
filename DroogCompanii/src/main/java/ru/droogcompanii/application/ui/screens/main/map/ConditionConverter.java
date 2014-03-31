package ru.droogcompanii.application.ui.screens.main.map;

import com.google.common.base.Optional;

import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerHierarchyContracts;

/**
 * Created by ls on 27.03.14.
 */
public class ConditionConverter {

    public static final PartnerHierarchyContracts.PartnersContract
            PARTNERS = new PartnerHierarchyContracts.PartnersContract();

    public static final PartnerHierarchyContracts.PartnerPointsContract
            PARTNER_POINTS = new PartnerHierarchyContracts.PartnerPointsContract();

    public static class ToReceivePartnerPoints {

        public static Optional<String> fromToReceivePartners(Optional<String> optionalCondition) {
            if (!optionalCondition.isPresent() || optionalCondition.get().trim().isEmpty()) {
                return optionalCondition;
            }
            String resultCondition = PARTNER_POINTS.COLUMN_NAME_PARTNER_ID + " IN (" +
                " SELECT " + PARTNERS.COLUMN_NAME_ID +
                " FROM " + PARTNERS.TABLE_NAME +
                " WHERE " + optionalCondition.get() +
            ")";
            return Optional.of(resultCondition);
        }
    }

}
