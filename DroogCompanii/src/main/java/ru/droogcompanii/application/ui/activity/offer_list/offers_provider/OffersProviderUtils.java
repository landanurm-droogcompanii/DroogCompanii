package ru.droogcompanii.application.ui.activity.offer_list.offers_provider;

import android.content.Context;

import ru.droogcompanii.application.data.db_util.offers.OffersReaderFromDatabase;
import ru.droogcompanii.application.data.db_util.offers.OffersWriterToDatabase;

/**
 * Created by ls on 14.02.14.
 */
public class OffersProviderUtils {

    private static final OffersProvider OFFERS_PROVIDER_FROM_INET = new DummyOffersProviderFromInet();

    public static void downloadOffersAndWriteToDatabaseIfThereIsNo(Context context) {
        OffersReaderFromDatabase readerFromDatabase = new OffersReaderFromDatabase(context);
        OffersProvider.Result result = new OffersResultImpl(readerFromDatabase.getOffers());
        if (result.isAbsent()) {
            downloadOffersAndWriteToDatabase(context);
        }
    }

    private static void downloadOffersAndWriteToDatabase(Context context) {
        OffersProvider.Result result = OFFERS_PROVIDER_FROM_INET.getOffers(context);
        if (result.isPresent()) {
            OffersWriterToDatabase writerToDatabase = new OffersWriterToDatabase(context);
            writerToDatabase.write(result.getOffers());
        }
    }
}
