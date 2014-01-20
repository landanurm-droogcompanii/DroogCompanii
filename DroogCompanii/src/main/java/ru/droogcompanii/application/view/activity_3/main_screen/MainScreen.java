package ru.droogcompanii.application.view.activity_3.main_screen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ru.droogcompanii.application.DroogCompaniiSharedPreferences;
import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerPointsReader;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnersReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable.filter.Filter;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.util.ListUtils;
import ru.droogcompanii.application.view.activity_3.filter_activity.FilterActivity;
import ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.StandardFiltersReader;
import ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.state.StandardFiltersState;
import ru.droogcompanii.application.view.activity_3.partner_activity.PartnerActivity;
import ru.droogcompanii.application.view.activity_3.search_activity.SearchActivity;
import ru.droogcompanii.application.view.fragment.partner_points_map_fragment.PartnerPointsMapFragment;
import ru.droogcompanii.application.view.fragment.partner_points_map_fragment.PartnerPointsProvider;

/**
 * Created by ls on 14.01.14.
 */
public class MainScreen extends android.support.v4.app.FragmentActivity
        implements PartnerPointsMapFragment.OnPartnerPointInfoWindowClickListener {


    private PartnerPointsMapFragment partnerPointsMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_main_screen);

        FragmentManager fragmentManager = getSupportFragmentManager();
        partnerPointsMapFragment =
                (PartnerPointsMapFragment) fragmentManager.findFragmentById(R.id.mapFragment);

        if (savedInstanceState == null) {
            setDefaultFilterState();
            initPartnerPointsMapFragment();
        }

        initListeners();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setDefaultFilterState() {
        SharedPreferences.Editor editor = DroogCompaniiSharedPreferences.get(this).edit();
        StandardFiltersState.DEFAULT.saveInto(editor);
        editor.commit();
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
        partnerPointsMapFragment.setFilters(readSavedFilters());
    }

    private List<Filter<PartnerPoint>> readSavedFilters() {
        SharedPreferences sharedPreferences = DroogCompaniiSharedPreferences.get(this);
        StandardFiltersReader filtersReader = new StandardFiltersReader(sharedPreferences);
        return filtersReader.read();
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

    private void onFilter() {
        Intent intent = new Intent(this, FilterActivity.class);
        startActivityForResult(intent, FilterActivity.REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == FilterActivity.REQUEST_CODE) && (resultCode == RESULT_OK)) {
            List<Filter<PartnerPoint>> returnedFilters = extractReturnedFilters(data);
            applyFilters(returnedFilters);
        }
    }

    private List<Filter<PartnerPoint>> extractReturnedFilters(Intent data) {
        List<Filter<PartnerPoint>> filters = null;
        if (data != null) {
            filters = (List<Filter<PartnerPoint>>) data.getSerializableExtra(Keys.filters);
        }
        if (filters == null) {
            throw new RuntimeException(FilterActivity.class.getName() + " doesn't return filters");
        }
        return filters;
    }

    private void applyFilters(List<Filter<PartnerPoint>> filters) {
        partnerPointsMapFragment.setFilters(filters);
    }

    private void onMenu() {
        // TODO:
        Toast.makeText(this, "Need to open Menu", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPartnerPointInfoWindowClick(PartnerPoint partnerPoint) {
        PartnersReader partnersReader = new PartnersReader(this);
        Partner partner = partnersReader.getPartnerOf(partnerPoint);

        PartnerPointsReader partnerPointsReader = new PartnerPointsReader(this);
        List<PartnerPoint> partnerPoints = partnerPointsReader.getPartnerPointsOf(partner);

        movePartnerPointAtFirstPosition(partnerPoint, partnerPoints);
        PartnerActivity.start(this, partner, partnerPoints);
    }

    private void movePartnerPointAtFirstPosition(PartnerPoint partnerPoint, List<PartnerPoint> partnerPoints) {
        int index = partnerPoints.indexOf(partnerPoint);
        if (index == -1) {
            throw new IllegalArgumentException("Partner points doesn't have this point");
        }
        ListUtils.swap(partnerPoints, 0, index);
    }
}
