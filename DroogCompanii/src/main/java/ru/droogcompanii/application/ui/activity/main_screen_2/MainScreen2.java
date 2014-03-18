package ru.droogcompanii.application.ui.activity.main_screen_2;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.util.able_to_start_task.ActivityAbleToStartTask;

/**
 * Created by ls on 14.03.14.
 */
public class MainScreen2 extends ActivityAbleToStartTask implements CategoryListFragment.Callbacks {

    private static final int DRAWER_GRAVITY = Gravity.START;


    private static final String TAG_CATEGORY_LIST = "TAG_CATEGORY_LIST";
    private static final String TAG_MAP = "TAG_MAP";

    private static final String KEY_IS_DRAWER_OPEN = "KEY_IS_DRAWER_OPEN";

    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_2);
        initNavigationDrawer();
        openNavigationDrawer();
        if (savedInstanceState == null) {
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
        transaction.add(R.id.contentFrame, new NewPartnerPointsMapFragment(), TAG_MAP);
        transaction.add(R.id.leftDrawer, new CategoryListFragment(), TAG_CATEGORY_LIST);
        transaction.commit();
    }

    private void openNavigationDrawer() {
        drawerLayout.openDrawer(DRAWER_GRAVITY);
    }

    private void closeNavigationDrawer() {
        drawerLayout.closeDrawer(DRAWER_GRAVITY);
    }


    private void initNavigationDrawer() {

        title = drawerTitle = getTitle();
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
                getSupportActionBar().setTitle(title);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(drawerTitle);
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
        String condition = findCategoryListFragment().getConditionToReceivePartners();
        findMapFragment().updateCondition(condition);
    }

    private CategoryListFragment findCategoryListFragment() {
        return (CategoryListFragment) getSupportFragmentManager().findFragmentByTag(TAG_CATEGORY_LIST);
    }

    private NewPartnerPointsMapFragment findMapFragment() {
        return (NewPartnerPointsMapFragment) getSupportFragmentManager().findFragmentByTag(TAG_MAP);
    }
}
