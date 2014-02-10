package ru.droogcompanii.application.ui.fragment.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;

/**
 * Created by ls on 21.01.14.
 */
class FilterSetImpl implements FilterSet, Serializable {
    private final List<SearchCriterion<PartnerPoint>> partnerPointSearchCriteria;
    private final List<Comparator<PartnerPoint>> partnerPointComparators;
    private final List<SearchCriterion<Partner>> partnerSearchCriteria;
    private final List<Comparator<Partner>> partnerComparators;

    public static FilterSetImpl from(List<Filter> filters) {
        FilterSetImpl result = new FilterSetImpl();
        for (Filter filter : filters) {
            filter.includeIn(result);
        }
        return result;
    }

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
    public List<? extends SearchCriterion<PartnerPoint>> getPartnerPointSearchCriteria() {
        return partnerPointSearchCriteria;
    }

    @Override
    public List<? extends Comparator<PartnerPoint>> getPartnerPointComparators() {
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
    public List<? extends SearchCriterion<Partner>> getPartnerSearchCriteria() {
        return partnerSearchCriteria;
    }

    @Override
    public List<? extends Comparator<Partner>> getPartnerComparators() {
        return partnerComparators;
    }
}
