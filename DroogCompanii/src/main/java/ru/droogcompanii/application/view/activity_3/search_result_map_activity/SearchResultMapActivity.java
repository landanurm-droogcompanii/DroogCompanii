package ru.droogcompanii.application.view.activity_3.search_result_map_activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.view.activity_3.ActivityWithPartnerPointsMapFragmentAndFilter;
import ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.filter_impl.StandardFiltersUtils;
import ru.droogcompanii.application.view.fragment.partner_points_map_fragment.PartnerPointsMapFragment;
import ru.droogcompanii.application.view.fragment.partner_points_map_fragment.PartnerPointsProvider;

/**
 * Created by ls on 14.01.14.
 */
public class SearchResultMapActivity extends ActivityWithPartnerPointsMapFragmentAndFilter
                    implements PartnerPointsMapFragment.OnPartnerPointInfoWindowClickListener {

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
        partnerPointsMapFragment.setFilters(StandardFiltersUtils.getCurrentFilters(this));
    }

    @Override
    public void onPartnerPointInfoWindowClick(PartnerPoint partnerPoint) {
        // TODO:
        Toast.makeText(this, partnerPoint.title, Toast.LENGTH_SHORT).show();
    }
}
