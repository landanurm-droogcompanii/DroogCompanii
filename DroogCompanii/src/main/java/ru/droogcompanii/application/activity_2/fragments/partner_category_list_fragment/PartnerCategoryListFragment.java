package ru.droogcompanii.application.activity_2.fragments.partner_category_list_fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;

import ru.droogcompanii.application.activity.partner_category_list_activity.PartnerCategoryListAdapter;
import ru.droogcompanii.application.data.data_structure.PartnerCategory;

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onPartnerCategoryClickListener = getOnPartnerCategoryClickListener();

        adapter = PartnerCategoryListAdapter.newInstance(getActivity(), savedInstanceState);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    private OnPartnerCategoryClickListener getOnPartnerCategoryClickListener() {
        Activity activity = getActivity();
        try {
            return (OnPartnerCategoryClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement " + OnPartnerCategoryClickListener.class.getName()
            );
        }
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
