package ru.droogcompanii.application.ui.activity.offer_list;

import android.content.Context;

import java.io.Serializable;

import ru.droogcompanii.application.ui.helpers.task.Task;

/**
 * Created by ls on 10.02.14.
 */
public class OffersProviderTask extends Task {

    private final OffersProvider offersProvider;

    public OffersProviderTask(Context context) {
        super();
        this.offersProvider = new DummyOffersProvider(context);
    }

    @Override
    protected Serializable doInBackground(Void... voids) {
        return (Serializable) offersProvider.getOffers();
    }
}
