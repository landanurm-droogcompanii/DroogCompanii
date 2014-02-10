package ru.droogcompanii.application.ui.activity.offer_list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.ui.activity.offer_details.OfferDetailsActivity;
import ru.droogcompanii.application.ui.fragment.offer_list.OfferListFragment;
import ru.droogcompanii.application.ui.helpers.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.ui.helpers.FragmentRemover;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;

/**
 * Created by ls on 31.01.14.
 */
public class OfferListActivity extends ActionBarActivityWithUpButton
                            implements OfferListFragment.Callbacks, TaskFragmentHolder.Callbacks {

    private static final String TAG_TASK_FRAGMENT_HOLDER = "TaskFragmentHolder";

    private OfferListFragment offerListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        FragmentManager fragmentManager = getSupportFragmentManager();
        offerListFragment = (OfferListFragment) fragmentManager.findFragmentById(R.id.offersFragment);

        if (savedInstanceState == null) {
            startDownloadingOffers();
        }
    }

    private void startDownloadingOffers() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment taskFragment = new OffersProviderTaskFragmentHolder();
        transaction.add(R.id.taskFragmentContainer, taskFragment, TAG_TASK_FRAGMENT_HOLDER);
        transaction.commit();
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
        List<Offer> offers = (List<Offer>) result;
        offerListFragment.setOffers(offers);
    }

    private void onTaskCancelled() {
        finish();
    }

    @Override
    public void onOfferItemClick(Offer offer) {
        OfferDetailsActivity.start(this, offer);
    }
}
