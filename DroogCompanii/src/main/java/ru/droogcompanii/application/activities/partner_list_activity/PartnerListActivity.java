package ru.droogcompanii.application.activities.partner_list_activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import ru.droogcompanii.application.activities.helpers.ActionBarListActivityWithBackButton;
import ru.droogcompanii.application.activities.partner_info_activity.PartnerInfoActivity;
import ru.droogcompanii.application.data.data_structure.Partner;
import ru.droogcompanii.application.data.data_structure.PartnerCategory;
import ru.droogcompanii.application.util.Keys;

public class PartnerListActivity extends ActionBarListActivityWithBackButton
                                implements AdapterView.OnItemClickListener {

    private PartnerCategory partnerCategory;
    private PartnerListAdapter adapter;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        partnerCategory = getPartnerCategory(savedInstanceState);
        setTitle(partnerCategory.title);
        adapter = new PartnerListAdapter(this, partnerCategory);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    private PartnerCategory getPartnerCategory(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return getPartnerCategory(getIntent());
        } else {
            return restorePartnerCategory(savedInstanceState);
        }
    }

    private PartnerCategory getPartnerCategory(Intent intent) {
        return (PartnerCategory) intent.getSerializableExtra(Keys.partnerCategory);
    }

    private PartnerCategory restorePartnerCategory(Bundle savedInstanceState) {
        return (PartnerCategory) savedInstanceState.getSerializable(Keys.partnerCategory);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partnerCategory, partnerCategory);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Partner partnerToShow = adapter.getItem(position);
        Intent intent = new Intent(this, PartnerInfoActivity.class);
        intent.putExtra(Keys.partner, partnerToShow);
        startActivity(intent);
    }
}
