package ru.droogcompanii.application.ui.fragment.offer_list;

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

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.ui.util.able_to_start_task.FragmentAbleToStartTask;
import ru.droogcompanii.application.ui.util.able_to_start_task.TaskNotBeInterrupted;

/**
 * Created by ls on 10.02.14.
 */
public class OfferListFragment extends FragmentAbleToStartTask implements AdapterView.OnItemClickListener {

    public static interface Callbacks {
        void onOfferItemClick(Offer offer);
    }

    private static final int TASK_REQUEST_CODE_RECEIVING_OFFERS = 461;

    private static final String KEY_OFFERS = "KEY_OFFERS";
    private static final String KEY_OFFERS_PROVIDER = "KEY_OFFERS_PROVIDER";

    private ArrayAdapter<Offer> adapter;
    private Callbacks callbacks;
    private GridView gridView;
    private Optional<List<Offer>> optionalOffers;


    public static Fragment newInstance(OfferType offerType, Optional<String> where) {
        OfferListFragment fragment = new OfferListFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_OFFERS_PROVIDER, new OffersReceiverByOfferType(offerType, where));
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
        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreState(savedInstanceState);
        }
        initList();
    }

    private void initStateByDefault() {
        optionalOffers = Optional.absent();
        startTaskReceivingOffers();
    }

    private void startTaskReceivingOffers() {
        OffersReceiverByOfferType offersProvider = (OffersReceiverByOfferType)
                getArguments().getSerializable(KEY_OFFERS_PROVIDER);
        TaskNotBeInterrupted task = new OffersReceiverTask(offersProvider, getActivity());
        startTask(TASK_REQUEST_CODE_RECEIVING_OFFERS, task);
    }

    private void restoreState(Bundle savedInstanceState) {
        optionalOffers = (Optional<List<Offer>>) savedInstanceState.getSerializable(KEY_OFFERS);
    }

    private void initList() {
        if (optionalOffers.isPresent()) {
            initList(optionalOffers.get());
        }
        gridView.setOnItemClickListener(this);
    }

    private void initList(List<Offer> offers) {
        View emptyListView = getView().findViewById(R.id.noOffersView);
        gridView.setEmptyView(emptyListView);
        adapter = new OffersAdapter(getActivity(), offers);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_OFFERS, optionalOffers);
    }

    public void setOffers(Serializable result) {
        List<Offer> offers = (List<Offer>) result;
        setOffers(offers);
    }

    private void setOffers(List<Offer> offers) {
        this.optionalOffers = Optional.of(offers);
        initList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Offer offer = adapter.getItem(position);
        callbacks.onOfferItemClick(offer);
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
        if (requestCode == TASK_REQUEST_CODE_RECEIVING_OFFERS) {
            onReceivingOffersTaskResult(resultCode, result);
        }
    }

    private void onReceivingOffersTaskResult(int resultCode, Serializable result) {
        if (resultCode == Activity.RESULT_OK) {
            setOffers(result);
        } else {
            getActivity().finish();
        }
    }
}
