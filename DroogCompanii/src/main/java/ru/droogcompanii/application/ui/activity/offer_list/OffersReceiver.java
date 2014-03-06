package ru.droogcompanii.application.ui.activity.offer_list;

import java.util.List;

import ru.droogcompanii.application.data.offers.Offer;

/**
 * Created by ls on 06.03.14.
 */
public interface OffersReceiver {
    List<Offer> getOffers();
}
