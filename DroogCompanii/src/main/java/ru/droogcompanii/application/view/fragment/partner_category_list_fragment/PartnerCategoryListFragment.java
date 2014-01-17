package ru.droogcompanii.application.view.fragment.partner_category_list_fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;

import ru.droogcompanii.application.view.activity.partner_category_list_activity.PartnerCategoryListAdapter;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;

/**
 * Created by ls on 10.01.14.
 */
public class PartnerCategoryListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    public static interface OnPartnerCategoryClickListener {
        void onPartnerCategoryClick(PartnerCategory partnerCategory);
    }

    private PartnerCategoryListAdapter adapter;
    private OnPartnerCategoryClickListener onPartnerCategoryClickListener;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onPartnerCategoryClickListener = (OnPartnerCategoryClickListener) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = PartnerCategoryListAdapter.newInstance(getActivity(), savedInstanceState);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.saveStateInto(outState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PartnerCategory partnerCategory = adapter.getItem(position);
        onPartnerCategoryClickListener.onPartnerCategoryClick(partnerCategory);
    }
}
