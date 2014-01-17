package ru.droogcompanii.application.view.activity.partner_list_activity;

import android.content.Context;

import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnersReader;
import ru.droogcompanii.application.view.helpers.SimpleArrayAdapter;

/**
 * Created by Leonid on 03.12.13.
 */
class PartnerListAdapter extends SimpleArrayAdapter<Partner> {

    public PartnerListAdapter(Context context, PartnerCategory partnerCategory) {
        super(context,
              getPartnersFromCategory(context, partnerCategory),
              new ItemToTitleConvertor<Partner>() {
                  @Override
                  public String getTitle(Partner item) {
                      return item.title;
                  }
              });
    }

    private static List<Partner> getPartnersFromCategory(Context context, PartnerCategory category) {
        PartnersReader partnersLoader = new PartnersReader(context);
        return partnersLoader.getPartnersOf(category);
    }
}
