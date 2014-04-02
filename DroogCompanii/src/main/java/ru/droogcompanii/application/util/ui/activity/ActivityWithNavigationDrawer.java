package ru.droogcompanii.application.util.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.ui.activity.menu_helper.ActivityMenuHelper;

/**
 * Created by ls on 24.03.14.
 */
public abstract class ActivityWithNavigationDrawer extends ActivityMenuHelper {

    private static final int DRAWER_GRAVITY = Gravity.START;

    private static class Key {
        public static final String IS_DRAWER_OPEN = "IS_DRAWER_OPEN" + Key.class.getName();
    }

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;

    private boolean initialized;
    private boolean drawerOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialized = false;
        drawerOpened = (savedInstanceState == null) ? false : wasNavigationDrawerOpen(savedInstanceState);
    }

    protected void initNavigationDrawer(int drawerLayoutId) {
        drawerLayout = (DrawerLayout) findViewById(drawerLayoutId);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_navigation_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerOpened = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerOpened = false;
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        drawerOpened = drawerLayout.isDrawerOpen(DRAWER_GRAVITY);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // need to open navigation drawer, because
        // otherwise a fragment inside the navigation
        // drawer will not be initialized
        openNavigationDrawer();

        initialized = true;
    }

    protected void restoreNavigationDrawerState(Bundle savedInstanceState) {
        if (!wasNavigationDrawerOpen(savedInstanceState)) {
            closeNavigationDrawer();
        }
    }

    protected boolean wasNavigationDrawerOpen(Bundle savedInstanceState) {
        return savedInstanceState.getBoolean(Key.IS_DRAWER_OPEN);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Key.IS_DRAWER_OPEN, isDrawerOpened());
    }

    protected boolean isDrawerOpened() {
        return drawerOpened;
    }

    protected void openNavigationDrawer() {
        drawerLayout.openDrawer(DRAWER_GRAVITY);
    }

    protected void closeNavigationDrawer() {
        drawerLayout.closeDrawer(DRAWER_GRAVITY);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (initialized) {
            drawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (initialized) {
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (initialized) {
            if (drawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
