package ru.droogcompanii.application.view.activity_3.main_screen;

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
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnersReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.ListUtils;
import ru.droogcompanii.application.view.activity_3.ActivityWithPartnerPointsMapFragmentAndFilter;
import ru.droogcompanii.application.view.activity_3.partner_activity.PartnerActivity;
import ru.droogcompanii.application.view.activity_3.search_activity.SearchActivity;
import ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.filter_impl.StandardFiltersUtils;
import ru.droogcompanii.application.view.fragment.partner_points_map_fragment.PartnerPointsMapFragment;
import ru.droogcompanii.application.view.fragment.partner_points_map_fragment.PartnerPointsProvider;

/**
 * Created by ls on 14.01.14.
 */
public class MainScreen extends ActivityWithPartnerPointsMapFragmentAndFilter
        implements PartnerPointsMapFragment.OnNeedToShowPartnerPointsListener {

    private PartnerPointsMapFragment partnerPointsMapFragment;

    @Override
    protected PartnerPointsMapFragment getPartnerPointsMapFragment() {
        return partnerPointsMapFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_main_screen);

        FragmentManager fragmentManager = getSupportFragmentManager();
        partnerPointsMapFragment =
                (PartnerPointsMapFragment) fragmentManager.findFragmentById(R.id.mapFragment);

        if (savedInstanceState == null) {
            StandardFiltersUtils.setDefaultFilters(this);
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
        partnerPointsMapFragment.setFilters(StandardFiltersUtils.getCurrentFilters(this));
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

    private void movePartnerPointAtFirstPosition(PartnerPoint partnerPoint, List<PartnerPoint> partnerPoints) {
        int index = partnerPoints.indexOf(partnerPoint);
        if (index == -1) {
            throw new IllegalArgumentException("Partner points doesn't have this point");
        }
        ListUtils.swap(partnerPoints, 0, index);
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
        String message = "Need to show " + partnerPointsToShow.size() + " partner points";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onNeedToShowPartnerPoint(PartnerPoint partnerPoint) {
        PartnersReader partnersReader = new PartnersReader(this);
        Partner partner = partnersReader.getPartnerOf(partnerPoint);

        PartnerPointsReader partnerPointsReader = new PartnerPointsReader(this);
        List<PartnerPoint> partnerPoints = partnerPointsReader.getPartnerPointsOf(partner);

        movePartnerPointAtFirstPosition(partnerPoint, partnerPoints);
        PartnerActivity.start(this, partner, partnerPoints);
    }
}
