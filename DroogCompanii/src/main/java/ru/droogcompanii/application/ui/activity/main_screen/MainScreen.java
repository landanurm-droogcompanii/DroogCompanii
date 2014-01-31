package ru.droogcompanii.application.ui.activity.main_screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.activity.activity_with_partner_points_map_fragment_and_info_panel.ActivityWithPartnerPointsMapFragmentAndInfoPanel;
import ru.droogcompanii.application.ui.activity.menu_activity.MenuActivity;
import ru.droogcompanii.application.ui.activity.search_activity.SearchActivity;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterUtils;
import ru.droogcompanii.application.ui.fragment.partner_points_map_fragment.PartnerPointsProvider;

/**
 * Created by ls on 14.01.14.
 */
public class MainScreen extends ActivityWithPartnerPointsMapFragmentAndInfoPanel {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main_screen;
    }

    @Override
    protected void onCreateActivity(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FilterUtils.resetFilters(this);
        }
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
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    @Override
    protected PartnerPointsProvider preparePartnerPointsProvider() {
        return new ClosePartnerPointsProvider();
    }
}
