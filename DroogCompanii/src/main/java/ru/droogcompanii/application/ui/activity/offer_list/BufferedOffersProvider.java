package ru.droogcompanii.application.ui.activity.offer_list;

import android.content.Context;

import com.google.common.base.Optional;

import java.io.Serializable;

import ru.droogcompanii.application.ui.activity.offer_list.offers_provider.OffersProvider;

/**
 * Created by ls on 06.03.14.
 */
public abstract class BufferedOffersProvider implements OffersProvider, Serializable {
    private Optional<Result> buffered;

    public BufferedOffersProvider() {
        buffered = Optional.absent();
    }

    @Override
    public Result getOffers(Context context) {
        if (buffered.isPresent()) {
            return buffered.get();
        }
        Result offers = receiveOffers(context);
        buffered = Optional.of(offers);
        return offers;
    }

    protected abstract Result receiveOffers(Context context);

    public void reset() {
        buffered = Optional.absent();
    }
}
