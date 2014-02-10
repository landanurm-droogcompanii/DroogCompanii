package ru.droogcompanii.application.ui.activity.search.search_result_provider_impl;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerPointsReader;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnersReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.search.SearchResultProvider;

/**
 * Created by ls on 28.01.14.
 */
public class SearchResultProviderByPartnerCategory implements SearchResultProvider, Serializable {
    private final PartnerCategory partnerCategory;

    public SearchResultProviderByPartnerCategory(PartnerCategory partnerCategory) {
        this.partnerCategory = partnerCategory;
    }

    @Override
    public String getTitle(Context context) {
        return partnerCategory.title;
    }

    @Override
    public List<Partner> getPartners(Context context) {
        PartnersReader reader = new PartnersReader(context);
        return reader.getPartnersOf(partnerCategory);
    }

    @Override
    public List<PartnerPoint> getPointsOfPartner(Context context, Partner partner) {
        PartnerPointsReader reader = new PartnerPointsReader(context);
        return reader.getPartnerPointsOf(partner);
    }

    @Override
    public List<PartnerPoint> getAllPartnerPoints(Context context) {
        PartnerPointsReader reader = new PartnerPointsReader(context);
        return reader.getPartnerPointsOf(partnerCategory);
    }
}