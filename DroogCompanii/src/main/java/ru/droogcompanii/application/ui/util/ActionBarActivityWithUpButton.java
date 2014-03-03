package ru.droogcompanii.application.ui.util;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import ru.droogcompanii.application.ui.activity.base_menu_helper.BaseActivityMenuHelper;
import ru.droogcompanii.application.ui.activity.base_menu_helper.DummyMenuHelper;
import ru.droogcompanii.application.ui.activity.base_menu_helper.MenuHelper;

/**
 * Created by ls on 24.12.13.
 */
public abstract class ActionBarActivityWithUpButton extends BaseActivityMenuHelper {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isUpButtonEnabled()) {
            initUpButtonOnActionBar();
        }
    }

    protected boolean isUpButtonEnabled() {
        return true;
    }

    private void initUpButtonOnActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onNeedToNavigateUp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onNeedToNavigateUp() {
        finish();
    }

    @Override
    protected MenuHelper getMenuHelper() {
        return new DummyMenuHelper();
    }
}