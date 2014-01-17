package ru.droogcompanii.application.view.activity_3.search_result_map_activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.view.fragment.partner_points_map_fragment.PartnerPointsMapFragment;
import ru.droogcompanii.application.view.fragment.partner_points_map_fragment.PartnerPointsProvider;

/**
 * Created by ls on 14.01.14.
 */
public class SearchResultMapActivity extends android.support.v4.app.FragmentActivity
                                    implements PartnerPointsMapFragment.OnPartnerPointInfoWindowClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_search_result_map);
        if (savedInstanceState == null) {
            initPartnerPointsMapFragment();
        }
        findViewById(R.id.filterButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFilter();
            }
        });
    }

    private void initPartnerPointsMapFragment() {
        Bundle args = getIntent().getExtras();
        PartnerPointsProvider partnerPointsProvider =
                (PartnerPointsProvider) args.getSerializable(Keys.partnerPointsProvider);
        FragmentManager fragmentManager = getSupportFragmentManager();
        PartnerPointsMapFragment fragment =
                (PartnerPointsMapFragment) fragmentManager.findFragmentById(R.id.mapFragment);
        fragment.setPartnerPointsProvider(partnerPointsProvider);
    }

    private void onFilter() {
        // TODO:
        Toast.makeText(this, "Need to open Filter", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPartnerPointInfoWindowClick(PartnerPoint partnerPoint) {
        // TODO:
        Toast.makeText(this, partnerPoint.title, Toast.LENGTH_SHORT).show();
    }
}
