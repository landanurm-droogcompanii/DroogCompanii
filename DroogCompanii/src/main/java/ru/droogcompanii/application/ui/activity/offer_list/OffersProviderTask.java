package ru.droogcompanii.application.ui.activity.offer_list;

import android.content.Context;

import java.io.Serializable;

import ru.droogcompanii.application.ui.activity.offer_list.offers_provider.OffersProvider;
import ru.droogcompanii.application.ui.helpers.task.Task;

/**
 * Created by ls on 10.02.14.
 */
public class OffersProviderTask extends Task {

    private final Context context;
    private final OffersProvider offersProvider;

    public OffersProviderTask(OffersProvider offersProvider, Context context) {
        super();
        this.context = context;
        this.offersProvider = offersProvider;
    }

    @Override
    protected Serializable doInBackground(Void... voids) {
        return (Serializable) offersProvider.getOffers(context);
    }
}
