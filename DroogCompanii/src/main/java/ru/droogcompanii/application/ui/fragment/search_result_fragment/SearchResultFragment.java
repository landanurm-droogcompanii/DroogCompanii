package ru.droogcompanii.application.ui.fragment.search_result_fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.ui.activity_3.search_result_activity.PartnerListAdapter;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 22.01.14.
 */
public class SearchResultFragment extends ListFragment
        implements AdapterView.OnItemClickListener {

    public static interface OnPartnerClickListener {
        void onPartnerClick(Partner partner);
    }

    private boolean isSearchResultReady;
    private List<Partner> partners;
    private OnPartnerClickListener onPartnerClickListener;
    private PartnerListAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onPartnerClickListener = (OnPartnerClickListener) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            initSearchResultIsNotReadyState();
        } else {
            restoreState(savedInstanceState);
        }

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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partners, (Serializable) partners);
        outState.putBoolean(Keys.isSearchResultReady, isSearchResultReady);
    }

    public void setSearchResult(List<Partner> partners) {
        this.partners = partners;
        isSearchResultReady = true;
        updateSearchResultList();
    }

    private void updateSearchResultList() {
        adapter = new PartnerListAdapter(getActivity(), partners);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Partner partner = adapter.getItem(position);
        onPartnerClickListener.onPartnerClick(partner);
    }

}
