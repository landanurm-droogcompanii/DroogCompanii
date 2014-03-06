package ru.droogcompanii.application.ui.activity.offer_list;

import android.content.Context;

import java.io.Serializable;

import ru.droogcompanii.application.ui.activity.offer_list.offers_provider.OffersProvider;
import ru.droogcompanii.application.ui.activity.able_to_start_task.TaskNotBeInterrupted;
import ru.droogcompanii.application.util.Snorlax;

/**
 * Created by ls on 10.02.14.
 */
public class OffersReceiverTask extends TaskNotBeInterrupted {

    private final Context context;
    private final OffersProvider offersProvider;

    public OffersReceiverTask(OffersProvider offersProvider, Context context) {
        super();
        this.context = context;
        this.offersProvider = offersProvider;
    }

    @Override
    protected Serializable doInBackground(Void... voids) {
        Snorlax.sleep();
        return (Serializable) offersProvider.getOffers(context);
    }
}
