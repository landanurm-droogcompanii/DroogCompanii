package ru.droogcompanii.application.ui.activity.offer_list.offers_provider;

import android.content.Context;

import ru.droogcompanii.application.data.db_util.offers.OffersReaderFromDatabase;
import ru.droogcompanii.application.data.db_util.offers.OffersWriterToDatabase;

/**
 * Created by ls on 11.02.14.
 */
public class OffersProviderViaDatabase implements OffersProvider {
    private final OffersProvider offersProvider;
    private final OffersReaderFromDatabase readerFromDatabase;
    private final OffersWriterToDatabase writerToDatabase;

    public OffersProviderViaDatabase(Context context, OffersProvider offersProvider) {
        this.offersProvider = offersProvider;
        this.writerToDatabase = new OffersWriterToDatabase(context);
        this.readerFromDatabase = new OffersReaderFromDatabase(context);
    }

    @Override
    public Result getOffers() {
        Result result = readerFromDatabase.getOffers();
        if (result.isAbsent()) {
            result = offersProvider.getOffers();
        }
        if (result.isPresent()) {
            writerToDatabase.write(result.getOffers());
        }
        return result;
    }
}
