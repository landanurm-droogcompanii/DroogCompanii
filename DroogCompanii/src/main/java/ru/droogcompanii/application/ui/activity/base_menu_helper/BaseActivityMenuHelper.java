package ru.droogcompanii.application.ui.activity.base_menu_helper;

import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

/**
 * Created by ls on 14.02.14.
 */
public abstract class BaseActivityMenuHelper extends ActionBarActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuHelper().init(menu);
        return true;
    }

    protected abstract MenuHelper getMenuHelper();
}
