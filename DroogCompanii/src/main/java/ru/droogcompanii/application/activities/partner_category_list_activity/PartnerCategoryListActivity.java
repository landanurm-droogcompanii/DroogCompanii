package ru.droogcompanii.application.activities.partner_category_list_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.io.Serializable;

import ru.droogcompanii.application.activities.helpers.ActionBarListActivity;
import ru.droogcompanii.application.activities.partner_list_activity.PartnerListActivity;
import ru.droogcompanii.application.data.data_structure.PartnerCategory;
import ru.droogcompanii.application.util.Keys;

public class PartnerCategoryListActivity extends ActionBarListActivity
                                      implements AdapterView.OnItemClickListener {

    private PartnerCategoryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = prepareAdapter(savedInstanceState);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    private PartnerCategoryListAdapter prepareAdapter(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return PartnerCategoryListAdapter.newInstance(this);
        } else {
            Serializable state = savedInstanceState.getSerializable(Keys.partnerCategoryListAdapterState);
            return PartnerCategoryListAdapter.newInstance(this, state);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partnerCategoryListAdapterState, adapter.getState());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PartnerCategory selectedCategory = adapter.getItem(position);
        Intent intent = new Intent(this, PartnerListActivity.class);
        intent.putExtra(Keys.partnerCategory, selectedCategory);
        startActivity(intent);
    }
}
