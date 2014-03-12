package ru.droogcompanii.application.ui.activity.partner_details;

import android.content.Context;

import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerPointsReader;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 12.03.14.
 */
public class PartnerAndPartnerPointsProviderByPartner
        implements PartnerDetailsActivity.PartnerAndPartnerPointsProvider, Serializable {
    private final int partnerId;
    private Optional<Partner> partner;

    public PartnerAndPartnerPointsProviderByPartner(Partner partner) {
        this.partner = Optional.of(partner);
        this.partnerId = 0;
    }

    public PartnerAndPartnerPointsProviderByPartner(int partnerId) {
        this.partner = Optional.absent();
        this.partnerId = partnerId;
    }

    @Override
    public Partner getPartner(Context context) {
        if (!partner.isPresent()) {
            readPartnerFromDB(context);
        }
        return partner.get();
    }

    private void readPartnerFromDB(Context context) {
        PartnersReader reader = new PartnersReader(context);
        partner = Optional.of(reader.getPartnerById(partnerId));
    }

    @Override
    public List<PartnerPoint> getPartnerPoints(Context context) {
        PartnerPointsReader reader = new PartnerPointsReader(context);
        if (partner.isPresent()) {
            return reader.getPartnerPointsOf(partner.get());
        } else {
            return reader.getPartnerPointsByPartnerId(partnerId);
        }
    }
}