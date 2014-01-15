package ru.droogcompanii.application.view.activity_3.main_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.data_structure.Partner;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerPointsReader;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnersReader;
import ru.droogcompanii.application.view.activity_3.filter_activity.FilterActivity;
import ru.droogcompanii.application.view.activity_3.partner_activity.PartnerActivity;
import ru.droogcompanii.application.view.activity_3.search_activity.SearchActivity;
import ru.droogcompanii.application.view.fragment.partner_points_map_fragment.PartnerPointsMapFragment;
import ru.droogcompanii.application.view.fragment.partner_points_map_fragment.PartnerPointsProvider;

/**
 * Created by ls on 14.01.14.
 */
public class MainScreen extends android.support.v4.app.FragmentActivity
        implements PartnerPointsMapFragment.OnPartnerPointInfoWindowClickListener {

    private static final PartnerPointsProvider ALL_PARTNER_POINTS_PROVIDER = new PartnerPointsProvider() {
        @Override
        public List<PartnerPoint> getPartnerPoints(Context context) {
            PartnerPointsReader reader = new PartnerPointsReader(context);
            return reader.getAllPartnerPoints();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_main_screen);
        if (savedInstanceState == null) {
            initPartnerPointsMapFragment();
        }
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

    private void initPartnerPointsMapFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        PartnerPointsMapFragment partnerPointsMapFragment =
                (PartnerPointsMapFragment) fragmentManager.findFragmentById(R.id.mapFragment);
        partnerPointsMapFragment.setPartnerPointsProvider(ALL_PARTNER_POINTS_PROVIDER);
    }

    private void onSearch() {
        startActivity(SearchActivity.class);
    }

    private void startActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    private void onFilter() {
        // TODO:
        Intent intent = new Intent(this, FilterActivity.class);
        startActivity(intent);
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
        if (partnerPoints.size() < 1) {
            return;
        }
        int index = partnerPoints.indexOf(partnerPoint);
        if (index == -1) {
            throw new IllegalArgumentException("Partner points doesn't have this point");
        }
        swap(partnerPoints, 0, index);
    }

    private static <T> void swap(List<T> list, int index1, int index2) {
        if (index1 == index2) {
            return;
        }
        T obj1 = list.get(index1);
        T obj2 = list.get(index2);
        list.set(index1, obj2);
        list.set(index2, obj1);
    }
}
