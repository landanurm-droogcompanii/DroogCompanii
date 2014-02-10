package ru.droogcompanii.application.ui.activity.offer_list;

import java.util.List;

import ru.droogcompanii.application.data.offers.Offer;

/**
 * Created by ls on 10.02.14.
 */
public interface OffersProvider {
    List<Offer> getOffers();
}
