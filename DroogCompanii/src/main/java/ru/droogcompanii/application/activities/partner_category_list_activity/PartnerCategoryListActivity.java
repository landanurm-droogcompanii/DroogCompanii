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

        if (savedInstanceState == null) {
            adapter = PartnerCategoryListAdapter.newInstance(this);
        } else {
            Serializable state = savedInstanceState.getSerializable(Keys.partnerCategoryListAdapterState);
            adapter = PartnerCategoryListAdapter.newInstance(this, state);
        }

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partnerCategoryListAdapterState, adapter.getState());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PartnerCategory selectedCategory = adapter.getItem(position);
        showPartnerCategory(selectedCategory);
    }

    private void showPartnerCategory(PartnerCategory partnerCategory) {
        Intent intent = new Intent(this, PartnerListActivity.class);
        intent.putExtra(Keys.partnerCategory, partnerCategory);
        startActivity(intent);
    }
}
