package ru.droogcompanii.application.ui.activity.search.search_result_provider_impl;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.FavoriteDBUtils;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerPointsReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.search.SearchResultProvider;

/**
 * Created by ls on 19.02.14.
 */
public class SearchResultProviderFavorite implements SearchResultProvider, Serializable {

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.favorite);
    }

    @Override
    public List<Partner> getPartners(Context context) {
        FavoriteDBUtils favoriteDBUtils = new FavoriteDBUtils(context);
        return favoriteDBUtils.getFavoritePartners();
    }

    @Override
    public List<PartnerPoint> getPointsOfPartner(Context context, Partner partner) {
        PartnerPointsReader reader = new PartnerPointsReader(context);
        return reader.getPartnerPointsOf(partner);
    }

    @Override
    public List<PartnerPoint> getAllPartnerPoints(Context context) {
        FavoriteDBUtils favoriteDBUtils = new FavoriteDBUtils(context);
        return favoriteDBUtils.getAllFavoritePartnerPoints();
    }
}