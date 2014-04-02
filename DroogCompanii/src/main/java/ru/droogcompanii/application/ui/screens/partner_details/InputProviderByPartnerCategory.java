package ru.droogcompanii.application.ui.screens.partner_details;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.screens.partner_list.PartnerListActivity;

/**
 * Created by ls on 11.03.14.
 */
public class InputProviderByPartnerCategory implements PartnerListActivity.InputProvider, Serializable {

    private final PartnerCategory category;

    public InputProviderByPartnerCategory(PartnerCategory category) {
        this.category = category;
    }

    @Override
    public List<Partner> getPartners(Context context) {
        PartnersReader partnersReader = new PartnersReader(context);
        return partnersReader.getPartnersOf(category);
    }

    @Override
    public String getTitle(Context context) {
        return null;
    }
}