package ru.droogcompanii.application.ui.activity_3.main_screen;

import android.content.Intent;
import android.location.Location;
import android.view.View;
import android.widget.Toast;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.activity_3.ActivityWithPartnerPointsMapFragmentAndInfoPanel;
import ru.droogcompanii.application.ui.activity_3.search_activity.SearchActivity;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterUtils;
import ru.droogcompanii.application.ui.fragment.filter_fragment.standard_filters.search_criteria_and_comparators.ComparatorByDistance;
import ru.droogcompanii.application.util.CurrentLocationProvider;

/**
 * Created by ls on 14.01.14.
 */
public class MainScreen extends ActivityWithPartnerPointsMapFragmentAndInfoPanel {

    @Override
    protected int getRootLayoutId() {
        return R.layout.activity_3_main_screen;
    }

    @Override
    protected void onFirstActivityLaunch() {
        FilterUtils.resetFilters(this);
        initPartnerPointsMapFragment();
    }

    private void initPartnerPointsMapFragment() {
        ComparatorByDistance.BaseLocationProvider baseLocationProvider = new ComparatorByDistance.BaseLocationProvider() {
            @Override
            public Location getBaseLocation() {
                return CurrentLocationProvider.get();
            }
        };
        partnerPointsMapFragment.setPartnerPointsProvider(new ClosePartnerPointsProvider(baseLocationProvider));
        partnerPointsMapFragment.setFilterSet(FilterUtils.getCurrentFilterSet(this));
    }

    @Override
    protected void onEachActivityLaunch() {
        initListeners();
    }

    private void initListeners() {
        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearch();
            }
        });
        findViewById(R.id.menuButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMenu();
            }
        });
    }

    private void onSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    private void onMenu() {
        // TODO:
        Toast.makeText(this, "Need to open Menu", Toast.LENGTH_SHORT).show();
    }
}
