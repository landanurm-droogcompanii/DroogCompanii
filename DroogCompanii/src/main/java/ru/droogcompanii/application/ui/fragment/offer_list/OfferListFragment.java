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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.ui.activity.offer_list.offers_provider.OffersProvider;

/**
 * Created by ls on 10.02.14.
 */
public class OfferListFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static interface Callbacks {
        void onOfferItemClick(Offer offer);
    }

    private static final List<Offer> NO_OFFERS = new ArrayList<Offer>();

    private static final String KEY_OFFERS = "KEY_OFFERS";

    private ArrayAdapter<Offer> adapter;
    private Callbacks callbacks;
    private GridView gridView;
    private List<Offer> offers;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_offers, container);
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
        offers = NO_OFFERS;
    }

    private void restoreState(Bundle savedInstanceState) {
        offers = (List<Offer>) savedInstanceState.getSerializable(KEY_OFFERS);
    }

    private void initList() {
        adapter = new OffersAdapter(getActivity(), offers);
        gridView.setOnItemClickListener(this);
        gridView.setEmptyView(prepareEmptyListView());
        gridView.setAdapter(adapter);
    }

    private View prepareEmptyListView() {
        return getActivity().findViewById(R.id.noOffersView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateInto(outState);
    }

    private void saveStateInto(Bundle outState) {
        outState.putSerializable(KEY_OFFERS, (Serializable) offers);
    }

    public void setOffers(Serializable result) {
        OffersProvider.Result offersResult = (OffersProvider.Result) result;
        setOffers(offersResult.getOffers());
    }

    private void setOffers(List<Offer> newOffers) {
        this.offers = newOffers;
        initList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Offer offer = adapter.getItem(position);
        callbacks.onOfferItemClick(offer);
    }
}
