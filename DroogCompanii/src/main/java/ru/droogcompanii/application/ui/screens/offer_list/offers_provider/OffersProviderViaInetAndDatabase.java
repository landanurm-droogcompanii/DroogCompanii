package ru.droogcompanii.application.ui.screens.offer_list.offers_provider;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.db_util.offers.OffersReaderFromDatabase;
import ru.droogcompanii.application.data.db_util.offers.OffersWriterToDatabase;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.util.DownloadingFailedException;

/**
 * Created by ls on 14.02.14.
 */
public class OffersProviderViaInetAndDatabase implements OffersProvider, Serializable {
    private static final OffersDownloader OFFERS_PROVIDER_FROM_INET = new DummyOffersDownloder();

    @Override
    public List<Offer> getAllOffers(Context context) {
        try {
            return downloadAndWriteOffersToDatabaseIfInetAvailable(context);
        } catch (DownloadingFailedException exception) {
            return tryReadAllOffersFromDatabase(context, exception);
        }
    }

    private static List<Offer> downloadAndWriteOffersToDatabaseIfInetAvailable(Context context) {
        List<Offer> offers = OFFERS_PROVIDER_FROM_INET.downloadOffers(context);
        OffersWriterToDatabase writerToDatabase = new OffersWriterToDatabase(context);
        writerToDatabase.write(offers);
        return offers;
    }

    private List<Offer> tryReadAllOffersFromDatabase(Context context, DownloadingFailedException exception) {
        List<Offer> offers = readOffersFromDatabaseByCondition(context, "");
        if (offers.isEmpty()) {
            throw exception;
        }
        return offers;
    }

    private static List<Offer> readOffersFromDatabaseByCondition(Context context, String where) {
        OffersReaderFromDatabase readerFromDatabase = new OffersReaderFromDatabase(context);
        return readerFromDatabase.getOfferList(where);
    }

    @Override
    public List<Offer> getOffersByCondition(Context context, String where) {
        try {
            downloadAndWriteOffersToDatabaseIfInetAvailable(context);
            return readOffersFromDatabaseByCondition(context, where);
        } catch (DownloadingFailedException exception) {
            return tryReadOffersFromDatabaseByCondition(context, where, exception);
        }
    }

    private List<Offer> tryReadOffersFromDatabaseByCondition(Context context,
                                  String where, DownloadingFailedException exception) {
        List<Offer> offers = readOffersFromDatabaseByCondition(context, where);
        if (offers.isEmpty()) {
            throw exception;
        }
        return offers;
    }

}
