package ru.droogcompanii.application.view.fragment.filter_fragment.filters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;

/**
 * Created by ls on 21.01.14.
 */
public class Filters implements Serializable {
    public final List<SearchableListing.SearchCriterion<PartnerPoint>> searchCriteria;
    public final List<Comparator<PartnerPoint>> comparators;

    public Filters() {
        searchCriteria = new ArrayList<SearchableListing.SearchCriterion<PartnerPoint>>();
        comparators = new ArrayList<Comparator<PartnerPoint>>();
    }

    public Filters(List<Filter> filters) {
        this();
        for (Filter filter : filters) {
            filter.includeInIfNeed(this);
        }
    }

    public void add(SearchableListing.SearchCriterion<PartnerPoint> searchCriterion) {
        searchCriteria.add(searchCriterion);
    }

    public void add(Comparator<PartnerPoint> comparator) {
        comparators.add(comparator);
    }

    public void add(Filters other) {
        searchCriteria.addAll(other.searchCriteria);
        comparators.addAll(other.comparators);
    }
}
