package ru.droogcompanii.application.ui.fragment.offer_list;

import android.content.Context;

import java.io.Serializable;

import ru.droogcompanii.application.ui.util.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;
import ru.droogcompanii.application.util.Snorlax;

/**
 * Created by ls on 10.02.14.
 */
public class OffersReceiverTask extends TaskNotBeInterruptedDuringConfigurationChange {
    private final Context context;
    private final OffersReceiverByOfferType offersReceiver;

    public OffersReceiverTask(OffersReceiverByOfferType offersReceiver, Context context) {
        this.context = context;
        this.offersReceiver = offersReceiver;
    }

    @Override
    protected Serializable doInBackground(Void... voids) {
        Snorlax.sleep();
        return (Serializable) offersReceiver.getOffers(context);
    }
}
