package ru.droogcompanii.application.view.activity_2.partner_category_list_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.view.activity_2.partner_category_map_activity.PartnerCategoryMapActivity;
import ru.droogcompanii.application.view.fragment.partner_category_list_fragment.PartnerCategoryListFragment;

public class PartnerCategoryListActivity2 extends ActionBarActivity implements PartnerCategoryListFragment.OnPartnerCategoryClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_partner_category_list);
    }

    @Override
    public void onPartnerCategoryClick(PartnerCategory partnerCategory) {
        Intent intent = new Intent(this, PartnerCategoryMapActivity.class);
        intent.putExtra(Keys.partnerCategory, partnerCategory);
        startActivity(intent);
    }
}
