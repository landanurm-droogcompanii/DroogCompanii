package ru.droogcompanii.application.ui.fragment.search_result_fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.ui.activity.search_result_activity.PartnerListAdapter;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 22.01.14.
 */
public class SearchResultFragment extends ListFragment
        implements AdapterView.OnItemClickListener {

    public static interface Callbacks {
        void onPartnerClick(Partner partner);
        void onFound();
        void onNotFound();
    }

    private boolean isSearchResultReady;
    private Callbacks callbacks;
    private List<Partner> partners;
    private PartnerListAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            initSearchResultIsNotReadyState();
        } else {
            restoreState(savedInstanceState);
        }

        getListView().setOnItemClickListener(this);

        if (isSearchResultReady) {
            updateSearchResultList();
        }
    }

    private void initSearchResultIsNotReadyState() {
        isSearchResultReady = false;
        partners = new ArrayList<Partner>();
    }

    private void restoreState(Bundle savedInstanceState) {
        partners = (List<Partner>) savedInstanceState.getSerializable(Keys.partners);
        isSearchResultReady = savedInstanceState.getBoolean(Keys.isSearchResultReady);

        int visibility = savedInstanceState.getInt(Keys.visibility);
        getView().setVisibility(visibility);
    }

    private View prepareEmptyView() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View emptyView = layoutInflater.inflate(R.layout.view_no_search_results, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        getActivity().addContentView(emptyView, layoutParams);
        return emptyView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partners, (Serializable) partners);
        outState.putBoolean(Keys.isSearchResultReady, isSearchResultReady);
        outState.putInt(Keys.visibility, getView().getVisibility());
    }

    public void setSearchResult(List<Partner> partners) {
        this.partners = partners;
        isSearchResultReady = true;
        updateSearchResultList();
    }

    private void updateSearchResultList() {
        getListView().setEmptyView(prepareEmptyView());
        adapter = new PartnerListAdapter(getActivity(), partners);
        setListAdapter(adapter);
        if (adapter.isEmpty()) {
            callbacks.onNotFound();
        } else {
            callbacks.onFound();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Partner partner = adapter.getItem(position);
        callbacks.onPartnerClick(partner);
    }

    public void show() {
        getView().setVisibility(View.VISIBLE);
    }

    public void hide() {
        getView().setVisibility(View.INVISIBLE);
    }

}
