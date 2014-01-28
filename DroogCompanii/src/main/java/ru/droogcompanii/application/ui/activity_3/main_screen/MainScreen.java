package ru.droogcompanii.application.ui.activity_3.main_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.Set;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerPointsReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity_3.ActivityWithPartnerPointsMapFragmentAndFilter;
import ru.droogcompanii.application.ui.activity_3.search_activity.SearchActivity;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterUtils;
import ru.droogcompanii.application.ui.fragment.partner_points_info_panel_fragment.PartnerPointsInfoPanelFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map_fragment.PartnerPointsMapFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map_fragment.PartnerPointsProvider;

/**
 * Created by ls on 14.01.14.
 */
public class MainScreen extends ActivityWithPartnerPointsMapFragmentAndFilter
        implements PartnerPointsMapFragment.OnNeedToShowPartnerPointsListener {

    private PartnerPointsMapFragment partnerPointsMapFragment;
    private PartnerPointsInfoPanelFragment partnerPointsInfoPanelFragment;

    @Override
    protected PartnerPointsMapFragment getPartnerPointsMapFragment() {
        return partnerPointsMapFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_main_screen);

        FragmentManager fragmentManager = getSupportFragmentManager();
        partnerPointsMapFragment = (PartnerPointsMapFragment)
                fragmentManager.findFragmentById(R.id.mapFragment);
        partnerPointsInfoPanelFragment = (PartnerPointsInfoPanelFragment)
                fragmentManager.findFragmentById(R.id.partnerPointsInfoPanelFragment);

        if (savedInstanceState == null) {
            FilterUtils.resetFilters(this);
            initPartnerPointsMapFragment();
        }

        initListeners();
    }

    private void initPartnerPointsMapFragment() {
        final PartnerPointsProvider allPartnerPointsProvider = new PartnerPointsProvider() {
            @Override
            public List<PartnerPoint> getPartnerPoints(Context context) {
                PartnerPointsReader reader = new PartnerPointsReader(context);
                return reader.getAllPartnerPoints();
            }
        };
        partnerPointsMapFragment.setPartnerPointsProvider(allPartnerPointsProvider);
        partnerPointsMapFragment.setFilterSet(FilterUtils.getCurrentFilterSet(this));
    }

    private void initListeners() {
        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearch();
            }
        });
        findViewById(R.id.filterButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFilter();
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

    @Override
    public void onNeedToShowPartnerPoints(Set<PartnerPoint> partnerPointsToShow) {
        partnerPointsInfoPanelFragment.setPartnerPoints(partnerPointsToShow);
    }

    @Override
    public void onNoLongerNeedToShowPartnerPoints() {
        partnerPointsInfoPanelFragment.hide();
    }
}
