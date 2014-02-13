package ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner_point;

import java.io.Serializable;
import java.util.Comparator;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 21.01.14.
 */
public class PartnerPointComparatorByTitle implements Comparator<PartnerPoint>, Serializable {
    @Override
    public int compare(PartnerPoint partnerPoint1, PartnerPoint partnerPoint2) {
        String title1 = partnerPoint1.getTitle().toLowerCase();
        String title2 = partnerPoint2.getTitle().toLowerCase();
        return title1.compareToIgnoreCase(title2);
    }
}
