package ru.droogcompanii.application.view.activity_3.main_screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerPointsReader;
import ru.droogcompanii.application.view.activity_3.search_activity.SearchActivity;
import ru.droogcompanii.application.view.fragment.partner_points_map_fragment.PartnerPointsMapFragment;
import ru.droogcompanii.application.view.fragment.partner_points_map_fragment.PartnerPointsProvider;

/**
 * Created by ls on 14.01.14.
 */
public class MainScreen extends android.support.v4.app.FragmentActivity
        implements PartnerPointsMapFragment.OnPartnerPointInfoWindowClickListener {

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
        PartnerPointsMapFragment fragment = (PartnerPointsMapFragment) fragmentManager.findFragmentById(R.id.mapFragment);
        fragment.setPartnerPointsProvider(new PartnerPointsProvider() {
            @Override
            public List<PartnerPoint> getPartnerPoints(Context context) {
                PartnerPointsReader reader = new PartnerPointsReader(context);
                return reader.getAllPartnerPoints();
            }
        });
    }

    @Override
    public void onPartnerPointInfoWindowClick(PartnerPoint partnerPoint) {
        // TODO:
        Toast.makeText(this, partnerPoint.title, Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "Need to open Filter", Toast.LENGTH_SHORT).show();
    }

    private void onMenu() {
        // TODO:
        Toast.makeText(this, "Need to open Menu", Toast.LENGTH_SHORT).show();
    }
}
