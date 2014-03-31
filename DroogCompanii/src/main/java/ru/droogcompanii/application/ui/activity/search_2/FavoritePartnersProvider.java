package ru.droogcompanii.application.ui.activity.search_2;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.FavoriteDBUtils;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.ui.activity.partner_list.PartnerListActivity;

/**
 * Created by ls on 19.02.14.
 */
public class FavoritePartnersProvider implements PartnerListActivity.InputProvider, Serializable {

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.favorite);
    }

    @Override
    public List<Partner> getPartners(Context context) {
        FavoriteDBUtils favoriteDBUtils = new FavoriteDBUtils(context);
        return favoriteDBUtils.getFavoritePartners();
    }
}