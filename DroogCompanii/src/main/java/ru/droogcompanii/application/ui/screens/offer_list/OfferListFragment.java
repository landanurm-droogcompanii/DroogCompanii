package ru.droogcompanii.application.ui.screens.offer_list;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.util.StateManager;
import ru.droogcompanii.application.util.ui.able_to_start_task.FragmentAbleToStartTask;
import ru.droogcompanii.application.util.ui.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;

/**
 * Created by ls on 10.02.14.
 */
public class OfferListFragment extends FragmentAbleToStartTask implements AdapterView.OnItemClickListener {

    public static interface Callbacks {
        void onOfferItemClick(Offer offer);
    }

    private static class TaskRequestCode {
        public static final int RECEIVING_OFFERS = 461;
    }

    private static class Key {
        private static final String OFFERS = "OFFERS";
        private static final String OFFERS_RECEIVER = "OFFERS_RECEIVER";
    }

    private ArrayAdapter<Offer> adapter;
    private Callbacks callbacks;
    private GridView gridView;
    private Optional<List<Offer>> optionalOffers;


    private final StateManager STATE_MANAGER = new StateManager() {
        @Override
        public void initStateByDefault() {
            optionalOffers = Optional.absent();
            startTaskReceivingOffers();
        }

        @Override
        public void restoreState(Bundle savedInstanceState) {
            optionalOffers = (Optional<List<Offer>>) savedInstanceState.getSerializable(Key.OFFERS);
        }

        @Override
        public void saveState(Bundle outState) {
            outState.putSerializable(Key.OFFERS, optionalOffers);
        }
    };

    private void startTaskReceivingOffers() {
        final OffersReceiverByOfferType offersReceiver =
                (OffersReceiverByOfferType) getArguments().getSerializable(Key.OFFERS_RECEIVER);
        startTask(TaskRequestCode.RECEIVING_OFFERS, new TaskNotBeInterruptedDuringConfigurationChange() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                return (Serializable) offersReceiver.getOffers(DroogCompaniiApplication.getContext());
            }
        });
    }


    public static Fragment newInstance(OfferType offerType, Optional<String> condition) {
        OfferListFragment fragment = new OfferListFragment();
        Bundle args = new Bundle();
        args.putSerializable(Key.OFFERS_RECEIVER, new OffersReceiverByOfferType(offerType, condition));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_offers, null);
        gridView = (GridView) root.findViewById(R.id.gridView);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        STATE_MANAGER.initState(savedInstanceState);
        initListIfNeed();
    }

    private void initListIfNeed() {
        if (optionalOffers.isPresent()) {
            initList();
        }
    }

    private void initList() {
        View emptyListView = getView().findViewById(R.id.noOffersView);
        gridView.setEmptyView(emptyListView);
        adapter = new OffersAdapter(getActivity(), optionalOffers.get());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        STATE_MANAGER.saveState(outState);
    }

    private void setOffers(List<Offer> offers) {
        optionalOffers = Optional.of(offers);
        initList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Offer offer = adapter.getItem(position);
        callbacks.onOfferItemClick(offer);
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
        if (requestCode == TaskRequestCode.RECEIVING_OFFERS) {
            onReceivingOffersTaskResult(resultCode, result);
        }
    }

    private void onReceivingOffersTaskResult(int resultCode, Serializable result) {
        if (resultCode != Activity.RESULT_OK) {
            getActivity().finish();
            return;
        }
        List<Offer> offers = (List<Offer>) result;
        setOffers(offers);
    }

}
