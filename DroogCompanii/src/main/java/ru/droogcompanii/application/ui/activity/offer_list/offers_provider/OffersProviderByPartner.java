package ru.droogcompanii.application.ui.activity.offer_list.offers_provider;

import android.content.Context;

import java.io.Serializable;

import ru.droogcompanii.application.data.db_util.offers.OffersReaderFromDatabase;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.ui.activity.offer_list.BufferedOffersProvider;

/**
 * Created by ls on 14.02.14.
 */
public class OffersProviderByPartner extends BufferedOffersProvider implements Serializable {
    private final Partner partner;

    public OffersProviderByPartner(Partner partner) {
        this.partner = partner;
    }

    @Override
    public Result receiveOffers(Context context) {
        OffersProviderUtils.downloadOffersAndWriteToDatabaseIfThereIsNo(context);
        OffersReaderFromDatabase reader = new OffersReaderFromDatabase(context);
        return new OffersResultImpl(reader.getOffersOf(partner));
    }
}
