package ru.droogcompanii.application.ui.activity.offers_activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.ui.fragment.offers_fragment.OffersFragment;
import ru.droogcompanii.application.ui.helpers.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.ui.helpers.FragmentRemover;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;

/**
 * Created by ls on 31.01.14.
 */
public class OffersActivity extends ActionBarActivityWithUpButton
                            implements OffersFragment.Callbacks, TaskFragmentHolder.Callbacks {

    private static final String TAG_TASK_FRAGMENT_HOLDER = "TaskFragmentHolder";

    private OffersFragment offersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        FragmentManager fragmentManager = getSupportFragmentManager();
        offersFragment = (OffersFragment) fragmentManager.findFragmentById(R.id.offersFragment);

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
        offersFragment.setOffers(offers);
    }

    private void onTaskCancelled() {
        finish();
    }

    @Override
    public void onOfferItemClick(Offer offer) {
        // TODO:
        Toast.makeText(this, "Need to show offer:\n" + offer.toString(), Toast.LENGTH_SHORT).show();
    }
}
