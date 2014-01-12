package ru.droogcompanii.application.activity_2.partner_category_list_activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.activity_2.fragments.partner_category_list_fragment.PartnerCategoryListFragment;
import ru.droogcompanii.application.activity_2.partner_category_map_activity.PartnerCategoryMapActivity;
import ru.droogcompanii.application.data.data_structure.PartnerCategory;
import ru.droogcompanii.application.util.Keys;

public class PartnerCategoryListActivity2 extends ActionBarActivity implements PartnerCategoryListFragment.OnPartnerCategoryClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_partner_category_list);
    }

    @Override
    public void onPartnerCategoryClick(PartnerCategory partnerCategory) {
        Intent intent = new Intent(this, PartnerCategoryMapActivity.class);
        intent.putExtra(Keys.partnerCategory, partnerCategory);
        startActivity(intent);
    }
}
