package ru.droogcompanii.application.view.activity.partner_list_activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import ru.droogcompanii.application.view.helpers.ActionBarListActivityWithBackButton;
import ru.droogcompanii.application.view.activity.partner_info_activity.PartnerInfoActivity;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.util.Keys;

public class PartnerListActivity extends ActionBarListActivityWithBackButton
                                implements AdapterView.OnItemClickListener {

    private PartnerCategory partnerCategory;
    private PartnerListAdapter adapter;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            partnerCategory = getPartnerCategory();
        } else {
            partnerCategory = restorePartnerCategory(savedInstanceState);
        }

        setTitle(partnerCategory.title);
        adapter = new PartnerListAdapter(this, partnerCategory);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    private PartnerCategory getPartnerCategory() {
        Intent intent = getIntent();
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
        Partner selectedPartner = adapter.getItem(position);
        showPartner(selectedPartner);
    }

    private void showPartner(Partner partner) {
        Intent intent = new Intent(this, PartnerInfoActivity.class);
        intent.putExtra(Keys.partner, partner);
        startActivity(intent);
    }
}
