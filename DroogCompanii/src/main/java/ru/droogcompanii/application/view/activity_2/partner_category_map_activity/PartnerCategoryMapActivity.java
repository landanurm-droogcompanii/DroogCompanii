package ru.droogcompanii.application.view.activity_2.partner_category_map_activity;

import android.os.Bundle;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.view.fragment.partner_category_map_fragment.PartnerCategoryMapFragment;
import ru.droogcompanii.application.data.data_structure.PartnerCategory;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 10.01.14.
 */
public class PartnerCategoryMapActivity extends android.support.v4.app.FragmentActivity
                   implements PartnerCategoryMapFragment.OnPartnerPointInfoWindowClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_partner_category_map);
        if (savedInstanceState == null) {
            PartnerCategory partnerCategory = (PartnerCategory) getIntent().getSerializableExtra(Keys.partnerCategory);
            preparePartnerCategoryMapFragment(partnerCategory);
        }
    }

    private void preparePartnerCategoryMapFragment(PartnerCategory partnerCategory) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        PartnerCategoryMapFragment fragment = (PartnerCategoryMapFragment) fragmentManager.findFragmentById(R.id.mapFragment);
        fragment.setPartnerCategory(partnerCategory);
    }

    @Override
    public void onPartnerPointInfoWindowClick(PartnerPoint partnerPoint) {

    }
}
