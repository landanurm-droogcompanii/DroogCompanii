package ru.droogcompanii.application.data.db_util.hierarchy_of_partners;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.db_util.SearchingInDBUtils;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 03.04.14.
 */
public class TextSearchInMaker {

    private static final String SEPARATOR_BETWEEN_PARTS = " ";

    public static String make(PartnerCategory partnerCategory) {
        return prepareTextSearchIn(
                partnerCategory.getTitle()
        );
    }

    public static String make(Partner partner) {
        return prepareTextSearchIn(
                partner.getTitle(),
                partner.getDiscountType()
        );
    }

    public static String make(PartnerPoint partnerPoint) {
        return prepareTextSearchIn(
                partnerPoint.getTitle(),
                partnerPoint.getAddress(),
                partnerPoint.getPaymentMethods()
        );
    }

    private static String prepareTextSearchIn(String... parts) {
        List<String> preparedParts = new ArrayList<String>(parts.length);
        int totalLength = 0;
        for (String eachPart : parts) {
            String preparedPart = SearchingInDBUtils.convertInTheSameCharacterCase(eachPart);
            preparedParts.add(preparedPart);
            totalLength += preparedPart.length();
        }
        int totalLengthOfSeparators = SEPARATOR_BETWEEN_PARTS.length() * (parts.length - 1);
        totalLength += totalLengthOfSeparators;
        return combine(preparedParts, totalLength);
    }
    
    private static String combine(List<String> parts, int totalLength) {
        StringBuilder builder = new StringBuilder(totalLength);
        boolean firstPart = true;
        for (String eachPart : parts) {
            if (firstPart) {
                firstPart = false;
            } else {
                builder.append(SEPARATOR_BETWEEN_PARTS);
            }
            builder.append(eachPart);
        }
        return builder.toString();
    }
}
