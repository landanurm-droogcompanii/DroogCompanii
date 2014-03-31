package ru.droogcompanii.application.util.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.ui.activity.menu_helper.ActivityMenuHelper;

/**
 * Created by ls on 24.03.14.
 */
public abstract class ActivityWithNavigationDrawer extends ActivityMenuHelper {

    private static final int DRAWER_GRAVITY = Gravity.START;

    private static class Key {
        public static final String IS_DRAWER_OPEN = "KEY_IS_DRAWER_OPEN";
    }

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;


    protected void initNavigationDrawer(int drawerLayoutId) {
        drawerLayout = (DrawerLayout) findViewById(drawerLayoutId);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.drawable.ic_navigation_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        );

        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // need to open navigation drawer, because
        // otherwise the category list fragment
        // inside the navigation drawer
        // will not be initialized
        openNavigationDrawer();
    }

    protected void restoreNavigationDrawerState(Bundle savedInstanceState) {
        if (savedInstanceState != null && !wasNavigationDrawerOpen(savedInstanceState)) {
            closeNavigationDrawer();
        }
    }

    protected boolean wasNavigationDrawerOpen(Bundle savedInstanceState) {
        return savedInstanceState.getBoolean(Key.IS_DRAWER_OPEN);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Key.IS_DRAWER_OPEN, isDrawerOpen());
    }

    protected boolean isDrawerOpen() {
        return drawerLayout.isDrawerOpen(DRAWER_GRAVITY);
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
}
