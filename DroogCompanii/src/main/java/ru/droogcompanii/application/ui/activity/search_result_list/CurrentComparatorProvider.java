package ru.droogcompanii.application.ui.activity.search_result_list;

import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.CombinedComparator;
import ru.droogcompanii.application.ui.fragment.filter.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter.FilterUtils;

/**
 * Created by ls on 17.02.14.
 */
public class CurrentComparatorProvider {

    public static Comparator<Partner> getCombinedPartnerComparator() {
        List<Comparator<Partner>> comparators = getCurrentFilterSet().getPartnerComparators();
        return CombinedComparator.from(comparators);
    }

    private static FilterSet getCurrentFilterSet() {
        return FilterUtils.getCurrentFilterSet(DroogCompaniiApplication.getContext());
    }

    public static Comparator<PartnerPoint> getCombinedPartnerPointComparator() {
        List<Comparator<PartnerPoint>> comparators = getCurrentFilterSet().getPartnerPointComparators();
        return new CombinedComparator(comparators);
    }
}
