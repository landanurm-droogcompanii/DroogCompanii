package ru.droogcompanii.application.ui.fragment.filter;

import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;

/**
 * Created by ls on 24.01.14.
 */
public interface FilterSet {
    void addPartnerPointSearchCriterion(SearchCriterion<PartnerPoint> searchCriterion);
    void addPartnerPointComparator(Comparator<PartnerPoint> comparator);
    void addPartnerSearchCriterion(SearchCriterion<Partner> searchCriterion);
    void addPartnerComparator(Comparator<Partner> comparator);
    SearchCriterion<PartnerPoint> getCombinedPartnerPointSearchCriterion();
    SearchCriterion<Partner> getCombinedPartnerSearchCriterion();
    List<Comparator<PartnerPoint>> getPartnerPointComparators();
    List<Comparator<Partner>> getPartnerComparators();

}
