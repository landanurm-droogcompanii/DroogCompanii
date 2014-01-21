package ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.search_criteria_and_comparators;

import java.io.Serializable;
import java.util.Comparator;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 21.01.14.
 */
public class ComparatorByTitle implements Comparator<PartnerPoint>, Serializable {
    @Override
    public int compare(PartnerPoint partnerPoint1, PartnerPoint partnerPoint2) {
        String title1 = partnerPoint1.title;
        String title2 = partnerPoint2.title;
        return title1.compareTo(title2);
    }
}
