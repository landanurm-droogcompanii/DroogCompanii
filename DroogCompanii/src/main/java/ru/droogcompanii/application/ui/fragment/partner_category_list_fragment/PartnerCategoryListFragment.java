package ru.droogcompanii.application.ui.fragment.partner_category_list_fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;

/**
 * Created by ls on 10.01.14.
 */
public class PartnerCategoryListFragment extends android.support.v4.app.ListFragment
        implements AdapterView.OnItemClickListener {

    public static interface Callbacks {
        void onPartnerCategoryClick(PartnerCategory partnerCategory);
    }

    private PartnerCategoryListAdapter adapter;
    private Callbacks callbacks;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemClickListener(this);

        adapter = PartnerCategoryListAdapter.newInstance(getActivity(), savedInstanceState);
        setListAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.saveStateInto(outState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PartnerCategory partnerCategory = adapter.getItem(position);
        callbacks.onPartnerCategoryClick(partnerCategory);
    }
}
