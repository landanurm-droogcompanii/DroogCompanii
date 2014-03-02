package ru.droogcompanii.application.ui.fragment.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.CombinedSearchCriterion;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;

/**
 * Created by ls on 21.01.14.
 */
class FilterSetImpl implements FilterSet, Serializable {
    private final List<SearchCriterion<PartnerPoint>> partnerPointSearchCriteria;
    private final List<Comparator<PartnerPoint>> partnerPointComparators;
    private final List<SearchCriterion<Partner>> partnerSearchCriteria;
    private final List<Comparator<Partner>> partnerComparators;


    public FilterSetImpl() {
        partnerPointSearchCriteria = new ArrayList<SearchCriterion<PartnerPoint>>();
        partnerPointComparators = new ArrayList<Comparator<PartnerPoint>>();
        partnerSearchCriteria = new ArrayList<SearchCriterion<Partner>>();
        partnerComparators = new ArrayList<Comparator<Partner>>();
    }

    public void addPartnerPointSearchCriterion(SearchCriterion<PartnerPoint> searchCriterion) {
        partnerPointSearchCriteria.add(searchCriterion);
    }

    public void addPartnerPointComparator(Comparator<PartnerPoint> comparator) {
        partnerPointComparators.add(comparator);
    }

    @Override
    public List<Comparator<PartnerPoint>> getPartnerPointComparators() {
        return partnerPointComparators;
    }

    @Override
    public void addPartnerSearchCriterion(SearchCriterion<Partner> searchCriterion) {
        partnerSearchCriteria.add(searchCriterion);
    }

    @Override
    public void addPartnerComparator(Comparator<Partner> comparator) {
        partnerComparators.add(comparator);
    }

    @Override
    public List<Comparator<Partner>> getPartnerComparators() {
        return partnerComparators;
    }

    @Override
    public SearchCriterion<Partner> getCombinedPartnerSearchCriterion() {
        return CombinedSearchCriterion.from(partnerSearchCriteria);
    }

    @Override
    public SearchCriterion<PartnerPoint> getCombinedPartnerPointSearchCriterion() {
        return CombinedSearchCriterion.from(partnerPointSearchCriteria);
    }
}
