package ru.droogcompanii.application.view.activity_3.search_result_map_activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import java.util.Set;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.view.activity_3.ActivityWithPartnerPointsMapFragmentAndFilter;
import ru.droogcompanii.application.view.fragment.filter_fragment.FilterUtils;
import ru.droogcompanii.application.view.fragment.partner_points_map_fragment.PartnerPointsMapFragment;
import ru.droogcompanii.application.view.fragment.partner_points_map_fragment.PartnerPointsProvider;

/**
 * Created by ls on 14.01.14.
 */
public class SearchResultMapActivity extends ActivityWithPartnerPointsMapFragmentAndFilter
                    implements PartnerPointsMapFragment.OnNeedToShowPartnerPointsListener {

    private PartnerPointsMapFragment partnerPointsMapFragment;

    @Override
    protected PartnerPointsMapFragment getPartnerPointsMapFragment() {
        return partnerPointsMapFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_search_result_map);

        FragmentManager fragmentManager = getSupportFragmentManager();
        partnerPointsMapFragment = (PartnerPointsMapFragment) fragmentManager.findFragmentById(R.id.mapFragment);

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
        partnerPointsMapFragment.setPartnerPointsProvider(partnerPointsProvider);
        partnerPointsMapFragment.setFilterSet(FilterUtils.getCurrentFilterSet(this));
    }

    @Override
    public void onNeedToShowPartnerPoints(Set<PartnerPoint> partnerPointsToShow) {
        String message = "Need to show " + partnerPointsToShow.size() + " partner points";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoLongerNeedToShowPartnerPoints() {
        // TODO: in the future
        // what need to do depends on all features of this activity
    }
}
