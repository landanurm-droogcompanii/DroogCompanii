package ru.droogcompanii.application.view.fragment.filter_fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;

/**
 * Created by ls on 21.01.14.
 */
public class FilterSetImpl implements FilterSet, Serializable {
    private final List<SearchCriterion<PartnerPoint>> searchCriteria;
    private final List<Comparator<PartnerPoint>> comparators;

    public static FilterSetImpl from(List<Filter> filters) {
        FilterSetImpl result = new FilterSetImpl();
        for (Filter filter : filters) {
            filter.includeIn(result);
        }
        return result;
    }

    public FilterSetImpl() {
        searchCriteria = new ArrayList<SearchCriterion<PartnerPoint>>();
        comparators = new ArrayList<Comparator<PartnerPoint>>();
    }

    public void add(SearchCriterion<PartnerPoint> searchCriterion) {
        searchCriteria.add(searchCriterion);
    }

    public void add(Comparator<PartnerPoint> comparator) {
        comparators.add(comparator);
    }

    public void add(FilterSet other) {
        searchCriteria.addAll(other.getSearchCriteria());
        comparators.addAll(other.getComparators());
    }

    @Override
    public List<? extends SearchCriterion<PartnerPoint>> getSearchCriteria() {
        return searchCriteria;
    }

    @Override
    public List<? extends Comparator<PartnerPoint>> getComparators() {
        return comparators;
    }
}
