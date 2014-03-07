package ru.droogcompanii.application.ui.activity.offer_list;

import android.content.Context;

import java.io.Serializable;

import ru.droogcompanii.application.data.db_util.offers.OffersReaderFromDatabase;
import ru.droogcompanii.application.ui.util.able_to_start_task.TaskNotBeInterrupted;
import ru.droogcompanii.application.util.LogUtils;
import ru.droogcompanii.application.util.Snorlax;

/**
 * Created by ls on 10.02.14.
 */
public class OffersReceiverTask extends TaskNotBeInterrupted {

    private final OffersReaderFromDatabase offersReader;
    private final String where;


    public OffersReceiverTask(String where, Context context) {
        this.where = where;
        this.offersReader = new OffersReaderFromDatabase(context);
    }

    @Override
    protected Serializable doInBackground(Void... voids) {
        LogUtils.debug("WHERE=>  " + where);
        Snorlax.sleep();
        return (Serializable) offersReader.getOfferList(where);
    }
}
