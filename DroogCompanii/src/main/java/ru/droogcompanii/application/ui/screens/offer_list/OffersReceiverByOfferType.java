package ru.droogcompanii.application.ui.screens.offer_list;

import android.content.Context;

import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.ui.screens.offer_list.offers_provider.OffersProvider;
import ru.droogcompanii.application.ui.screens.offer_list.offers_provider.OffersProviderViaInetAndDatabase;

/**
 * Created by Leonid on 08.03.14.
 */
public class OffersReceiverByOfferType implements Serializable {

    private static final OffersProvider OFFERS_PROVIDER = new OffersProviderViaInetAndDatabase();

    private final Optional<String> condition;

    public OffersReceiverByOfferType(OfferType offerType, Optional<String> condition) {
        this.condition = OffersConditionsPreparer.prepare(offerType, condition);
    }

    public List<Offer> getOffers(Context context) {
        if (condition.isPresent()) {
            return OFFERS_PROVIDER.getOffersByCondition(context, condition.get());
        } else {
            return OFFERS_PROVIDER.getAllOffers(context);
        }
    }

}
