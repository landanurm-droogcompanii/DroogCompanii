package ru.droogcompanii.application.ui.screens.partner_details;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Optional;

import java.io.Serializable;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.data.db_util.offers.OffersReaderFromDatabase;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.util.StateManager;
import ru.droogcompanii.application.util.ui.able_to_start_task.FragmentAbleToStartTask;
import ru.droogcompanii.application.util.ui.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;

/**
 * Created by ls on 27.03.14.
 */
public class PartnerDetailsFragment extends FragmentAbleToStartTask {

    public static interface Callbacks {
        void onPartnerDetailsLoaded();
    }

    private static final Callbacks DUMMY_CALLBACKS = new Callbacks() {
        @Override
        public void onPartnerDetailsLoaded() {
            // do nothing
        }
    };

    private static class Key {
        public static final String INPUT_PROVIDER = "INPUT_PROVIDER";
        public static final String PARTNER = "PARTNER";
        public static final String HAS_OFFERS = "HAS_OFFERS";
    }

    private static class RequestCode {
        public static final int RECEIVING_DETAILS = 216;
    }

    public static PartnerDetailsFragment newInstance(PartnerDetailsActivity.InputProvider inputProvider) {
        Bundle args = new Bundle();
        args.putSerializable(Key.INPUT_PROVIDER, (Serializable) inputProvider);
        PartnerDetailsFragment fragment = new PartnerDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private Callbacks callbacks;

    private boolean hasOffers;
    private Optional<Partner> partner;
    private PartnerDetailsDisplay partnerDetailsDisplay;


    private final StateManager STATE_MANAGER = new StateManager() {
        @Override
        public void initStateByDefault() {
            partner = Optional.absent();
            hasOffers = false;
            startTaskReceivingDetails();
        }

        @Override
        public void restoreState(Bundle savedInstanceState) {
            partner = (Optional<Partner>) savedInstanceState.getSerializable(Key.PARTNER);
            hasOffers = savedInstanceState.getBoolean(Key.HAS_OFFERS);
            if (partner.isPresent()) {
                displayDetails();
            }
        }

        @Override
        public void saveState(Bundle outState) {
            outState.putSerializable(Key.PARTNER, partner);
            outState.putBoolean(Key.HAS_OFFERS, hasOffers);
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        callbacks = DUMMY_CALLBACKS;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        partnerDetailsDisplay = new PartnerDetailsDisplay(getActivity(), inflater);
        return partnerDetailsDisplay.getView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        STATE_MANAGER.initState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        STATE_MANAGER.saveState(outState);
    }


    private static class TaskResult implements Serializable {
        public final boolean hasOffers;
        public final Partner partner;

        public TaskResult(Partner partner, boolean hasOffers) {
            this.partner = partner;
            this.hasOffers = hasOffers;
        }
    }

    private void startTaskReceivingDetails() {
        Bundle args = getArguments();
        final PartnerDetailsActivity.InputProvider inputProvider =
                (PartnerDetailsActivity.InputProvider) args.getSerializable(Key.INPUT_PROVIDER);
        startTask(RequestCode.RECEIVING_DETAILS, new TaskNotBeInterruptedDuringConfigurationChange() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                Partner partner = inputProvider.getPartner(DroogCompaniiApplication.getContext());
                boolean hasOffers = defineHasOffers(partner);
                return new TaskResult(partner, hasOffers);
            }
        });
    }

    private boolean defineHasOffers(Partner partner) {
        OffersReaderFromDatabase offersReader = new OffersReaderFromDatabase(DroogCompaniiApplication.getContext());
        return offersReader.hasOffers(partner);
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
        if (requestCode == RequestCode.RECEIVING_DETAILS) {
            onReceivingDetailsTaskFinished(resultCode, result);
        }
    }

    private void onReceivingDetailsTaskFinished(int resultCode, Serializable result) {
        if (resultCode != Activity.RESULT_OK) {
            getActivity().finish();
            return;
        }
        onDetailsReceived(result);
        callbacks.onPartnerDetailsLoaded();
    }

    private void onDetailsReceived(Serializable result) {
        TaskResult taskResult = (TaskResult) result;
        partner = Optional.of(taskResult.partner);
        hasOffers = taskResult.hasOffers;
        displayDetails();
    }

    private void displayDetails() {
        partnerDetailsDisplay.display(partner.get(), hasOffers);
    }
}
