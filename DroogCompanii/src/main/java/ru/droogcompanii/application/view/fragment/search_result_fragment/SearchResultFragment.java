package ru.droogcompanii.application.view.fragment.search_result_fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.view.activity_3.search_activity.SearchResultProvider;
import ru.droogcompanii.application.view.activity_3.search_result_activity.PartnerListAdapter;

/**
 * Created by ls on 22.01.14.
 */
public class SearchResultFragment extends ListFragment implements AdapterView.OnItemClickListener {

    public static interface OnPartnerClickListener {
        void onPartnerClick(Partner partner);
    }

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

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }

        adapter = new PartnerListAdapter(getActivity(), partners);

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    public void setSearchResultProvider(SearchResultProvider searchResultProvider) {
        partners = searchResultProvider.getPartners(getActivity());
    }

    private void restoreState(Bundle savedInstanceState) {
        partners = (List<Partner>) savedInstanceState.getSerializable(Keys.partners);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partners, (Serializable) partners);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Partner partner = adapter.getItem(position);
        onPartnerClickListener.onPartnerClick(partner);
    }

}
