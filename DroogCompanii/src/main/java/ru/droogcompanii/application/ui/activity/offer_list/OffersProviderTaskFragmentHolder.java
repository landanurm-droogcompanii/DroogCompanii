package ru.droogcompanii.application.ui.activity.offer_list;

import android.app.Activity;
import android.os.Bundle;

import ru.droogcompanii.application.ui.activity.offer_list.offers_provider.OffersProvider;
import ru.droogcompanii.application.ui.helpers.task.Task;
import ru.droogcompanii.application.ui.helpers.task.TaskFragment;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;

/**
 * Created by ls on 10.02.14.
 */
public class OffersProviderTaskFragmentHolder extends TaskFragmentHolder {

    private OffersProvider offersProvider;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        this.offersProvider = (OffersProvider) args.getSerializable(OfferListActivity.KEY_OFFERS_PROVIDER);
    }

    @Override
    protected Integer getTaskDialogTitleId() {
        return TaskFragment.NO_TITLE_ID;
    }

    @Override
    protected Task prepareTask() {
        return new OffersProviderTask(offersProvider, getActivity());
    }
}
