package ru.droogcompanii.application.ui.activity_3.search_activity;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerPointsReader;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnersReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;
import ru.droogcompanii.application.ui.activity_3.search_activity.search_by_query_criterion.SearchByQueryCriterion;

/**
 * Created by ls on 28.01.14.
 */
public class SearchResultProviderBySearchQuery implements SearchResultProvider, Serializable {

    private final SearchByQueryCriterion searchCriterion;

    private static class PartnerPointSearchCriterion implements SearchCriterion<PartnerPoint> {
        private final SearchByQueryCriterion searchCriterion;

        public PartnerPointSearchCriterion(SearchByQueryCriterion searchCriterion) {
            this.searchCriterion = searchCriterion;
        }

        @Override
        public boolean meetCriterion(PartnerPoint partnerPoint) {
            return searchCriterion.meetCriterion(partnerPoint.title);
        }
    }

    private static class PartnerSearchCriterion implements SearchCriterion<Partner> {
        private final SearchByQueryCriterion searchCriterion;
        private final PartnerPointsReader partnerPointsReader;
        private final PartnerPointSearchCriterion partnerPointsSearchCriterion;

        public PartnerSearchCriterion(SearchByQueryCriterion searchCriterion, PartnerPointsReader partnerPointsReader) {
            this.searchCriterion = searchCriterion;
            this.partnerPointsReader = partnerPointsReader;
            this.partnerPointsSearchCriterion = new PartnerPointSearchCriterion(searchCriterion);
        }

        @Override
        public boolean meetCriterion(Partner partner) {
            return searchCriterion.meetCriterion(partner.title) ||
                    partnerPointsOfPartnerMeetCriterion(partner);
        }

        private boolean partnerPointsOfPartnerMeetCriterion(Partner partner) {
            List<PartnerPoint> partnerPoints = partnerPointsReader.getPartnerPointsOf(partner);
            for (PartnerPoint partnerPoint : partnerPoints) {
                if (partnerPointsSearchCriterion.meetCriterion(partnerPoint)) {
                    return true;
                }
            }
            return false;
        }
    }

    public SearchResultProviderBySearchQuery(String searchQuery) {
        searchCriterion = new SearchByQueryCriterion(searchQuery);
    }

    @Override
    public List<Partner> getPartners(Context context) {
        PartnersReader reader = new PartnersReader(context);
        List<Partner> partners = reader.getAllPartners();
        SearchableListing<Partner> searchable = SearchableListing.newInstance(partners);
        searchable.addSearchCriterion(preparePartnerSearchCriterion(context));
        return searchable.toList();
    }

    private SearchCriterion<Partner> preparePartnerSearchCriterion(Context context) {
        return new PartnerSearchCriterion(searchCriterion, new PartnerPointsReader(context));
    }

    @Override
    public List<PartnerPoint> getPartnerPoints(Context context, Partner partner) {
        PartnerPointsReader reader = new PartnerPointsReader(context);
        return filterPartnerPoints(reader.getPartnerPointsOf(partner));
    }

    private List<PartnerPoint> filterPartnerPoints(List<PartnerPoint> partnerPoints) {
        SearchableListing<PartnerPoint> searchable = SearchableListing.newInstance(partnerPoints);
        searchable.addSearchCriterion(new PartnerPointSearchCriterion(searchCriterion));
        return searchable.toList();
    }

    @Override
    public List<PartnerPoint> getAllPartnerPoints(Context context) {
        PartnerPointsReader reader = new PartnerPointsReader(context);
        return filterPartnerPoints(reader.getAllPartnerPoints());
    }
}
