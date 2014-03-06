package ru.droogcompanii.application.ui.fragment.offer_list;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.google.common.base.Optional;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.data.offers.Offers;
import ru.droogcompanii.application.ui.activity.able_to_start_task.FragmentAbleToStartTask;
import ru.droogcompanii.application.ui.activity.able_to_start_task.TaskNotBeInterrupted;
import ru.droogcompanii.application.ui.activity.offer_list.OfferListActivity;
import ru.droogcompanii.application.ui.activity.offer_list.OffersReceiverTask;
import ru.droogcompanii.application.ui.activity.offer_list.offers_provider.OffersProvider;

/**
 * Created by ls on 10.02.14.
 */
public class OfferListFragment extends FragmentAbleToStartTask implements AdapterView.OnItemClickListener {

    private static final String KEY_OFFERS = "KEY_OFFERS";


    public static OfferListFragment newInstance(OffersProvider offersProvider) {
        OfferListFragment fragment = new OfferListFragment();
        Bundle args = new Bundle();
        args.putSerializable(OfferListActivity.KEY_OFFERS_PROVIDER, (Serializable) offersProvider);
        fragment.setArguments(args);
        return fragment;
    }

    public static interface Callbacks {
        void onOfferItemClick(Offer offer);
    }

    private ArrayAdapter<Offer> adapter;
    private Callbacks callbacks;
    private GridView gridView;
    private Optional<Offers> optionalOffers;

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
        OffersProvider offersProvider =
                (OffersProvider) getArguments().getSerializable(OfferListActivity.KEY_OFFERS_PROVIDER);
        TaskNotBeInterrupted task = new OffersReceiverTask(offersProvider, getActivity());
        startTask(task);
    }

    private void restoreState(Bundle savedInstanceState) {
        optionalOffers = (Optional<Offers>) savedInstanceState.getSerializable(KEY_OFFERS);
    }

    private void initList() {
        gridView.setOnItemClickListener(this);
        if (optionalOffers.isPresent()) {
            initList(optionalOffers.get());
        }
    }

    private void initList(Offers offers) {
        gridView.setEmptyView(getEmptyListView());
        adapter = new OffersAdapter(getActivity(), offers);
        gridView.setAdapter(adapter);
    }

    private View getEmptyListView() {
        return getActivity().findViewById(R.id.noOffersView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateInto(outState);
    }

    private void saveStateInto(Bundle outState) {
        outState.putSerializable(KEY_OFFERS, optionalOffers);
    }

    public void setOffers(Serializable result) {
        OffersProvider.Result offersResult = (OffersProvider.Result) result;
        setOffers(offersResult.getOffers());
    }

    private void setOffers(Offers newOffers) {
        this.optionalOffers = Optional.of(newOffers);
        initList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Offer offer = adapter.getItem(position);
        callbacks.onOfferItemClick(offer);
    }

    @Override
    protected void onResult(int resultCode, Serializable result) {
        if (resultCode != Activity.RESULT_OK) {
            getActivity().finish();
            return;
        }
        setOffers(result);
    }
}
