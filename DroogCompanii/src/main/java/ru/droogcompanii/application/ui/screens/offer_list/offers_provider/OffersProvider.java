package ru.droogcompanii.application.ui.screens.offer_list.offers_provider;

import android.content.Context;

import java.util.List;

import ru.droogcompanii.application.data.offers.Offer;

/**
 * Created by ls on 10.02.14.
 */
public interface OffersProvider {
    List<Offer> getAllOffers(Context context);
    List<Offer> getOffersByCondition(Context context, String where);
}
