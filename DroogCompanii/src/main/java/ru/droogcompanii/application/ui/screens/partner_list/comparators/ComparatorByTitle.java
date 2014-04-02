package ru.droogcompanii.application.ui.screens.partner_list.comparators;

import java.io.Serializable;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;

/**
 * Created by ls on 02.04.14.
 */
public class ComparatorByTitle extends BaseComparator<Partner> implements Serializable {
    @Override
    public int compare(Partner partner1, Partner partner2) {
        String title1 = partner1.getTitle();
        String title2 = partner2.getTitle();
        return title1.compareToIgnoreCase(title2);
    }
}
