package ru.droogcompanii.application.activity_2.fragments.partner_category_map_fragment;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.data_structure.Partner;
import ru.droogcompanii.application.data.data_structure.PartnerCategory;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerPointsReader;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnersReader;

/**
 * Created by Leonid on 11.01.14.
 */
public class PartnerPointsProvider {
    private final Context context;

    public PartnerPointsProvider(Context context) {
        this.context = context;
    }

    public List<PartnerPoint> getPartnerPointsOf(Partner partner) {
        PartnerPointsReader partnerPointsReader = new PartnerPointsReader(context);
        return partnerPointsReader.getPartnerPointsOf(partner);
    }

    public List<PartnerPoint> getPartnerPointsOf(PartnerCategory partnerCategory) {
        PartnersReader partnersReader = new PartnersReader(context);
        List<Partner> partners = partnersReader.getPartners(partnerCategory);
        List<PartnerPoint> result = new ArrayList<PartnerPoint>();
        for (Partner partner : partners) {
            result.addAll(getPartnerPointsOf(partner));
        }
        return result;
    }
}
