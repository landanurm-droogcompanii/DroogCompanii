package ru.droogcompanii.application.ui.activity.offer_list;

import android.content.Context;

import java.io.Serializable;

import ru.droogcompanii.application.ui.activity.offer_list.offers_provider.OffersProvider;
import ru.droogcompanii.application.ui.activity.offer_list.offers_provider.DummyOffersDownloader;
import ru.droogcompanii.application.ui.activity.offer_list.offers_provider.OffersProviderViaDatabase;
import ru.droogcompanii.application.ui.helpers.task.Task;

/**
 * Created by ls on 10.02.14.
 */
public class OffersProviderTask extends Task {

    private final OffersProvider offersProvider;

    public OffersProviderTask(Context context) {
        super();
        this.offersProvider = new OffersProviderViaDatabase(context, new DummyOffersDownloader(context));
    }

    @Override
    protected Serializable doInBackground(Void... voids) {
        return (Serializable) offersProvider.getOffers();
    }
}
