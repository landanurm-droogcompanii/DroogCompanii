package ru.droogcompanii.application.ui.activity.offer_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.ui.activity.offer_details.OfferDetailsActivity;
import ru.droogcompanii.application.ui.activity.offer_list.offers_provider.AllOffersProvider;
import ru.droogcompanii.application.ui.activity.offer_list.offers_provider.OffersProvider;
import ru.droogcompanii.application.ui.fragment.offer_list.OfferListFragment;
import ru.droogcompanii.application.ui.util.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.ui.activity.able_to_start_task.TaskNotBeInterrupted;

/**
 * Created by ls on 31.01.14.
 */
public class OfferListActivity extends ActionBarActivityWithUpButton
                            implements OfferListFragment.Callbacks {

    public static final String KEY_OFFERS_PROVIDER = "OffersProvider";

    private static final int TASK_REQUEST_CODE_OFFERS_RECEIVING = 251;

    private OfferListFragment offerListFragment;

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

        FragmentManager fragmentManager = getSupportFragmentManager();
        offerListFragment = (OfferListFragment) fragmentManager.findFragmentById(R.id.offersFragment);

        if (savedInstanceState == null) {
            startTask();
        }
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
    public void onOfferItemClick(Offer offer) {
        OfferDetailsActivity.start(this, offer);
    }
}
