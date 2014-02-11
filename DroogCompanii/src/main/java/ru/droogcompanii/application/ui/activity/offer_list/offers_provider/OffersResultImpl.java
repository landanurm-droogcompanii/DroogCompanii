package ru.droogcompanii.application.ui.activity.offer_list.offers_provider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.offers.Offer;

/**
 * Created by ls on 11.02.14.
 */
public class OffersResultImpl implements OffersProvider.Result, Serializable {

    private final List<Offer> offers;

    public static OffersResultImpl noOffers() {
        return new OffersResultImpl(new ArrayList<Offer>());
    }

    public OffersResultImpl(List<Offer> offers) {
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
    public List<Offer> getOffers() {
        return offers;
    }
}
