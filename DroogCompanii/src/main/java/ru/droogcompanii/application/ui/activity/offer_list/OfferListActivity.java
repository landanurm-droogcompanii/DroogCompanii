package ru.droogcompanii.application.ui.activity.offer_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.ui.activity.offer_details.OfferDetailsActivity;
import ru.droogcompanii.application.ui.activity.offer_list.offers_provider.AllOffersProvider;
import ru.droogcompanii.application.ui.activity.offer_list.offers_provider.OffersProvider;
import ru.droogcompanii.application.ui.fragment.offer_list.OfferListFragment;
import ru.droogcompanii.application.ui.helpers.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.ui.helpers.FragmentRemover;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;

/**
 * Created by ls on 31.01.14.
 */
public class OfferListActivity extends ActionBarActivityWithUpButton
                            implements OfferListFragment.Callbacks, TaskFragmentHolder.Callbacks {

    public static final String KEY_OFFERS_PROVIDER = "OffersProvider";

    private static final String TAG_TASK_FRAGMENT_HOLDER = "TaskFragmentHolder";

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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment taskFragment = new OffersProviderTaskFragmentHolder();
        taskFragment.setArguments(prepareTaskArguments());
        transaction.add(R.id.taskFragmentContainer, taskFragment, TAG_TASK_FRAGMENT_HOLDER);
        transaction.commit();
    }

    private Bundle prepareTaskArguments() {
        Serializable offersProvider = getIntent().getSerializableExtra(KEY_OFFERS_PROVIDER);
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_OFFERS_PROVIDER, offersProvider);
        return bundle;
    }

    @Override
    public void onTaskFinished(int resultCode, Serializable result) {
        if (resultCode == RESULT_OK) {
            onTaskFinishedSuccessfully(result);
        } else {
            onTaskCancelled();
        }
    }

    private void onTaskFinishedSuccessfully(Serializable result) {
        FragmentRemover.removeFragmentByTag(this, TAG_TASK_FRAGMENT_HOLDER);
        offerListFragment.setOffers(result);
    }

    private void onTaskCancelled() {
        finish();
    }

    @Override
    public void onOfferItemClick(Offer offer) {
        OfferDetailsActivity.start(this, offer);
    }
}
