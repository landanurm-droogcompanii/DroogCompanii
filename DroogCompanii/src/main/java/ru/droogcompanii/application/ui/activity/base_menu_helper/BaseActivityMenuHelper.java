package ru.droogcompanii.application.ui.activity.base_menu_helper;

import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

/**
 * Created by ls on 14.02.14.
 */
public abstract class BaseActivityMenuHelper extends ActionBarActivity {

    private Menu menu;

    protected Menu getMenu() {
        return menu;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuHelper().init(menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuHelper().prepare(menu);
        return true;
    }

    protected abstract MenuHelper getMenuHelper();
}
