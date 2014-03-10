package ru.droogcompanii.application.ui.activity.offer_list.offers_provider;

import android.content.Context;

import java.util.List;

import ru.droogcompanii.application.data.offers.Offer;

/**
 * Created by Leonid on 08.03.14.
 */
public interface OffersDownloader {
    List<Offer> downloadOffers(Context context);
}
