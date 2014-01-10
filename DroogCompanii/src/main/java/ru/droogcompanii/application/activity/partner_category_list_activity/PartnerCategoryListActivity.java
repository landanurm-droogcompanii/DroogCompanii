package ru.droogcompanii.application.activity.partner_category_list_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.io.Serializable;

import ru.droogcompanii.application.activity.helpers.ActionBarListActivity;
import ru.droogcompanii.application.activity.partner_list_activity.PartnerListActivity;
import ru.droogcompanii.application.data.data_structure.PartnerCategory;
import ru.droogcompanii.application.util.Keys;

public class PartnerCategoryListActivity extends ActionBarListActivity
                                      implements AdapterView.OnItemClickListener {

    private PartnerCategoryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = PartnerCategoryListAdapter.newInstance(this, savedInstanceState);

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.saveStateInto(outState);
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
