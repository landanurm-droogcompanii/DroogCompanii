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
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 10.02.14.
 */
public class OfferListFragment extends Fragment implements AdapterView.OnItemClickListener {


    private static final int ITEMS_PER_ROW_IN_LANDSCAPE = 2;
    private static final int CELL_SPACING_IN_LANDSCAPE = 6;

    public static interface Callbacks {
        void onOfferItemClick(Offer offer);
    }

    private static final List<Offer> NO_OFFERS = new ArrayList<Offer>();

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
        adapter = prepareAdapter(savedInstanceState);
        gridView.setOnItemClickListener(this);
        setListAdapter(adapter);
    }

    private void setListAdapter(ArrayAdapter<Offer> adapter) {
        gridView.setAdapter(adapter);
    }

    private ArrayAdapter<Offer> prepareAdapter(Bundle savedInstanceState) {
        List<Offer> newOffers = (savedInstanceState == null)
                ? NO_OFFERS : (List<Offer>) savedInstanceState.getSerializable(Keys.offers);
        return prepareAdapter(newOffers);
    }

    private ArrayAdapter<Offer> prepareAdapter(List<Offer> newOffers) {
        this.offers = newOffers;
        return new OffersAdapter(getActivity(), newOffers);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.offers, (Serializable) offers);
    }

    public void setOffers(List<Offer> newOffers) {
        adapter = prepareAdapter(newOffers);
        setListAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Offer offer = adapter.getItem(position);
        callbacks.onOfferItemClick(offer);
    }
}
