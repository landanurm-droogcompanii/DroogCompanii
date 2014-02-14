package ru.droogcompanii.application.ui.activity.offer_list.offers_provider;

import android.content.Context;

import java.io.Serializable;

import ru.droogcompanii.application.data.db_util.offers.OffersReaderFromDatabase;

/**
 * Created by ls on 11.02.14.
 */
public class AllOffersProvider implements OffersProvider, Serializable {

    @Override
    public Result getOffers(Context context) {
        OffersProviderUtils.downloadOffersAndWriteToDatabaseIfThereIsNo(context);
        OffersReaderFromDatabase readerFromDatabase = new OffersReaderFromDatabase(context);
        return new OffersResultImpl(readerFromDatabase.getOffers());
    }
}
