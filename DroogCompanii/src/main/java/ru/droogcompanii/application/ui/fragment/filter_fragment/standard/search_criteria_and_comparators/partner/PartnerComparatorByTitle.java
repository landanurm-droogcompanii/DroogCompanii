package ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators.partner;

import java.io.Serializable;
import java.util.Comparator;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;

/**
 * Created by ls on 21.01.14.
 */
public class PartnerComparatorByTitle implements Comparator<Partner>, Serializable {
    @Override
    public int compare(Partner partner1, Partner partner2) {
        String title1 = partner1.title;
        String title2 = partner2.title;
        return title1.compareToIgnoreCase(title2);
    }
}
