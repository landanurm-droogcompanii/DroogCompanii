package ru.droogcompanii.application.ui.activity.main_screen_2;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.google.common.base.Optional;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.main_screen_2.category_list.CategoryListFragment;
import ru.droogcompanii.application.ui.activity.main_screen_2.details.PartnerPointDetailsFragment;
import ru.droogcompanii.application.ui.activity.main_screen_2.map.NewPartnerPointsMapFragment;
import ru.droogcompanii.application.ui.util.able_to_start_task.ActivityAbleToStartTask;
import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by ls on 14.03.14.
 */
public class MainScreen2 extends ActivityAbleToStartTask
        implements CategoryListFragment.Callbacks, NewPartnerPointsMapFragment.Callbacks {

    private static final int DRAWER_GRAVITY = Gravity.START;


    private static final String TAG_CATEGORY_LIST = "TAG_CATEGORY_LIST";
    private static final String TAG_MAP = "TAG_MAP";
    private static final String TAG_PARTNER_POINT_DETAILS = "TAG_PARTNER_POINT_DETAILS";

    private static final String KEY_IS_DRAWER_OPEN = "KEY_IS_DRAWER_OPEN";

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_2);
        initNavigationDrawer();
        if (savedInstanceState == null) {
            openNavigationDrawer();
            placeFragmentsOnLayout();
        } else {
            restoreNavigationDrawerState(savedInstanceState);
        }
    }

    private void restoreNavigationDrawerState(Bundle savedInstanceState) {
        boolean isNeedToOpenDrawer = savedInstanceState.getBoolean(KEY_IS_DRAWER_OPEN);
        if (isNeedToOpenDrawer) {
            openNavigationDrawer();
        } else {
            closeNavigationDrawer();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        boolean isDrawerOpen = drawerLayout.isDrawerOpen(DRAWER_GRAVITY);
        outState.putBoolean(KEY_IS_DRAWER_OPEN, isDrawerOpen);
    }

    private void placeFragmentsOnLayout() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.containerOfMapFragment, new NewPartnerPointsMapFragment(), TAG_MAP);
        transaction.add(R.id.leftDrawer, new CategoryListFragment(), TAG_CATEGORY_LIST);
        transaction.add(R.id.containerOfPartnerPointDetailsFragment,
                new PartnerPointDetailsFragment(), TAG_PARTNER_POINT_DETAILS);
        transaction.commit();
    }

    private void openNavigationDrawer() {
        drawerLayout.openDrawer(DRAWER_GRAVITY);
    }

    private void closeNavigationDrawer() {
        drawerLayout.closeDrawer(DRAWER_GRAVITY);
    }


    private void initNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_navigation_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReceivingCategoriesTaskCompleted() {
        // skip
    }

    @Override
    public void onListInitialized() {
        updateMapFragment();
    }

    @Override
    public void onCurrentCategoryChanged() {
        updateMapFragment();
    }

    private void updateMapFragment() {
        CategoryListFragment categoryListFragment = findCategoryListFragment();
        Optional<String> condition = categoryListFragment.getConditionToReceivePartners();
        NewPartnerPointsMapFragment mapFragment = findMapFragment();
        mapFragment.updateCondition(condition);
        if (condition.isPresent()) {
            setTitle(categoryListFragment.getSelectedCategoryName());
        }
    }

    private CategoryListFragment findCategoryListFragment() {
        return (CategoryListFragment) getSupportFragmentManager().findFragmentByTag(TAG_CATEGORY_LIST);
    }

    private NewPartnerPointsMapFragment findMapFragment() {
        return (NewPartnerPointsMapFragment) getSupportFragmentManager().findFragmentByTag(TAG_MAP);
    }

    @Override
    public void onDisplayDetails(List<PartnerPoint> partnerPoints) {
        LogUtils.debug("onDisplayDetails()");
        findPartnerPointDetailsFragment().setPartnerPoints(partnerPoints);
    }

    private PartnerPointDetailsFragment findPartnerPointDetailsFragment() {
        return (PartnerPointDetailsFragment) getSupportFragmentManager().findFragmentByTag(TAG_PARTNER_POINT_DETAILS);
    }

    @Override
    public void onHideDetails() {
        LogUtils.debug("onHideDetails()");
        findPartnerPointDetailsFragment().hide();
    }
}
