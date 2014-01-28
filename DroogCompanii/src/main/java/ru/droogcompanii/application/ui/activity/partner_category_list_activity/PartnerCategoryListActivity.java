package ru.droogcompanii.application.ui.activity.partner_category_list_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.ui.activity.partner_list_activity.PartnerListActivity;
import ru.droogcompanii.application.ui.helpers.ActionBarListActivity;

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
