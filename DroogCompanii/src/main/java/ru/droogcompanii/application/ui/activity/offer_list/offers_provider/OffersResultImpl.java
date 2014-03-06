package ru.droogcompanii.application.ui.activity.offer_list.offers_provider;

import java.io.Serializable;

import ru.droogcompanii.application.data.offers.Offers;

/**
 * Created by ls on 11.02.14.
 */
public class OffersResultImpl implements OffersProvider.Result, Serializable {

    private final Offers offers;

    public static OffersResultImpl noOffers() {
        return new OffersResultImpl(new Offers());
    }

    public OffersResultImpl(Offers offers) {
        this.offers = offers;
    }

    @Override
    public boolean isAbsent() {
        return (offers == null) || offers.isEmpty();
    }

    @Override
    public boolean isPresent() {
        return !isAbsent();
    }

    @Override
    public Offers getOffers() {
        return offers;
    }
}
