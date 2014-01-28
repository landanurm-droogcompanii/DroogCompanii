package ru.droogcompanii.application.ui.fragment.filter_fragment;

import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;

/**
 * Created by ls on 24.01.14.
 */
public interface FilterSet {
    void add(SearchCriterion<PartnerPoint> searchCriterion);
    void add(Comparator<PartnerPoint> comparator);
    void add(FilterSet other);
    List<? extends SearchCriterion<PartnerPoint>> getSearchCriteria();
    List<? extends Comparator<PartnerPoint>> getComparators();
}
