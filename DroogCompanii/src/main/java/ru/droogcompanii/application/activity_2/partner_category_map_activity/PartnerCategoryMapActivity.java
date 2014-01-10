package ru.droogcompanii.application.activity_2.partner_category_map_activity;

import android.os.Bundle;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.activity_2.fragments.partner_category_map_fragment.PartnerCategoryMapFragment;
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
        setContentView(R.layout.activity_partner_category_map);
        if (savedInstanceState == null) {
            PartnerCategory partnerCategory = (PartnerCategory) getIntent().getSerializableExtra(Keys.partnerCategory);
            createPartnerCategoryMapFragment(partnerCategory);
        }
    }

    private void createPartnerCategoryMapFragment(PartnerCategory partnerCategory) {
        android.support.v4.app.Fragment fragment = PartnerCategoryMapFragment.newInstance(partnerCategory);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.rootLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onPartnerPointInfoWindowClick(PartnerPoint partnerPoint) {

    }
}
