package ru.droogcompanii.application.ui.activity.offer_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.ui.activity.able_to_start_task.TaskNotBeInterrupted;
import ru.droogcompanii.application.ui.activity.offer_details.OfferDetailsActivity;
import ru.droogcompanii.application.ui.activity.offer_list.offers_provider.AllOffersProvider;
import ru.droogcompanii.application.ui.activity.offer_list.offers_provider.OffersProvider;
import ru.droogcompanii.application.ui.activity.offer_list.offers_provider.OffersProviderByPartner;
import ru.droogcompanii.application.ui.fragment.offer_list.OfferListFragment;
import ru.droogcompanii.application.ui.util.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.util.CurrentMethodNameLogger;

/**
 * Created by ls on 31.01.14.
 */
public class OfferListActivity extends ActionBarActivityWithUpButton
                            implements OfferListFragment.Callbacks {

    private final CurrentMethodNameLogger LOGGER = new CurrentMethodNameLogger(getClass());

    public static final String KEY_OFFERS_PROVIDER = "OffersProvider";

    private static final int TASK_REQUEST_CODE_OFFERS_RECEIVING = 251;

    private static final String TAG_OFFER_LIST_FRAGMENT = "TAG_OFFER_LIST_FRAGMENT";


    private static enum Mode {
        ALL_OFFERS,
        SPECIFIED_BY_PARTNER
    }

    private OfferListFragment offerListFragment;

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
        LOGGER.log();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_list);

        if (savedInstanceState == null) {
            init();
        }

        /*
        FragmentManager fragmentManager = getSupportFragmentManager();
        offerListFragment = (OfferListFragment) fragmentManager.findFragmentById(R.id.offersFragment);

        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreState(savedInstanceState);
        }

        if (savedInstanceState == null) {
            startTask();
        }
        */
    }

    private void init() {
        LOGGER.log();

        OffersProvider offersProvider = (OffersProvider) getIntent().getSerializableExtra(KEY_OFFERS_PROVIDER);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.containerOfOfferListFragment, OfferListFragment.newInstance(offersProvider));
        transaction.commit();
    }

    private void startTask() {
        OffersProvider offersProvider = (OffersProvider) getIntent().getSerializableExtra(KEY_OFFERS_PROVIDER);
        TaskNotBeInterrupted task = new OffersReceiverTask(offersProvider, this);
        startTask(TASK_REQUEST_CODE_OFFERS_RECEIVING, task);
    }

    @Override
    protected void onReceiveResult(int requestCode, int resultCode, Serializable result) {
        if (requestCode == TASK_REQUEST_CODE_OFFERS_RECEIVING) {
            onReceivingOffersTaskFinished(resultCode, result);
        }
    }

    private void onReceivingOffersTaskFinished(int resultCode, Serializable result) {
        if (resultCode == RESULT_OK) {
            onReceivingOffersTaskFinishedSuccessfully(result);
        } else {
            onReceivingOffersTaskCancelled();
        }
    }

    private void onReceivingOffersTaskFinishedSuccessfully(Serializable result) {
        offerListFragment.setOffers(result);
    }

    private void onReceivingOffersTaskCancelled() {
        finish();
    }

    @Override
    protected void onStop() {
        LOGGER.log();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LOGGER.log();

        super.onDestroy();
    }

    @Override
    public void onOfferItemClick(Offer offer) {
        OfferDetailsActivity.start(this, offer);
    }
}
