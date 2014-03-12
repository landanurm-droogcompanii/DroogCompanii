package ru.droogcompanii.application.ui.activity.partner_details;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerPointsReader;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.ListUtils;

/**
 * Created by ls on 12.03.14.
 */
public class PartnerAndPartnerPointsProviderByPartnerPoint implements PartnerDetailsActivity.PartnerAndPartnerPointsProvider, Serializable {
    private final PartnerPoint partnerPoint;

    public PartnerAndPartnerPointsProviderByPartnerPoint(PartnerPoint partnerPoint) {
        this.partnerPoint = partnerPoint;
    }

    @Override
    public Partner getPartner(Context context) {
        PartnersReader partnersReader = new PartnersReader(context);
        return partnersReader.getPartnerOf(partnerPoint);
    }

    @Override
    public List<PartnerPoint> getPartnerPoints(Context context) {
        PartnerPointsReader partnerPointsReader = new PartnerPointsReader(context);
        Partner partner = getPartner(context);
        List<PartnerPoint> partnerPoints = partnerPointsReader.getPartnerPointsOf(partner);
        ListUtils.moveElementAtFirstPosition(partnerPoint, partnerPoints);
        return partnerPoints;
    }
}