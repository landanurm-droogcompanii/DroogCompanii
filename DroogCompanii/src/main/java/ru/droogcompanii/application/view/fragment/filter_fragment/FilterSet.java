package ru.droogcompanii.application.view.fragment.filter_fragment;

import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;

/**
 * Created by ls on 24.01.14.
 */
public interface FilterSet {
    void add(SearchableListing.SearchCriterion<PartnerPoint> searchCriterion);
    void add(Comparator<PartnerPoint> comparator);
    void add(FilterSet other);
    List<? extends SearchableListing.SearchCriterion<PartnerPoint>> getSearchCriteria();
    List<? extends Comparator<PartnerPoint>> getComparators();
}
