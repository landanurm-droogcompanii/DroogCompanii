package ru.droogcompanii.application.ui.activity.offer_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.ui.activity.offer_details.OfferDetailsActivity;
import ru.droogcompanii.application.ui.activity.offer_list.offers_provider.AllOffersProvider;
import ru.droogcompanii.application.ui.activity.offer_list.offers_provider.OffersProvider;
import ru.droogcompanii.application.ui.activity.offer_list.offers_provider.OffersProviderByPartner;
import ru.droogcompanii.application.ui.fragment.offer_list.OfferListFragment;
import ru.droogcompanii.application.ui.util.ActionBarActivityWithUpButton;

/**
 * Created by ls on 31.01.14.
 */
public class OfferListActivity extends ActionBarActivityWithUpButton
                            implements OfferListFragment.Callbacks {

    public static final String KEY_OFFERS_PROVIDER = "OffersProvider";

    private static final String TAG_OFFER_LIST_FRAGMENT = "TAG_OFFER_LIST_FRAGMENT";

    public static void start(Context context, Partner partner) {
        start(context, new OffersProviderByPartner(partner));
    }

    public static void start(Context context) {
        start(context, new AllOffersProvider());
    }

    public static void start(Context context, OffersProvider offersProvider) {
        Intent intent = new Intent(context, OfferListActivity.class);
        intent.putExtra(KEY_OFFERS_PROVIDER, (Serializable) offersProvider);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_list);

        if (savedInstanceState == null) {
            init();
        }

    }

    private void init() {
        placeOfferListFragmentOnLayout();
    }

    private void placeOfferListFragmentOnLayout() {
        OffersProvider offersProvider = (OffersProvider) getIntent().getSerializableExtra(KEY_OFFERS_PROVIDER);
        OfferListFragment fragment = OfferListFragment.newInstance(offersProvider);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.containerOfOfferListFragment, fragment, TAG_OFFER_LIST_FRAGMENT);
        transaction.commit();
    }

    @Override
    public void onOfferItemClick(Offer offer) {
        OfferDetailsActivity.start(this, offer);
    }
}
