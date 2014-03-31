package ru.droogcompanii.application.util.ui.activity.menu_helper;

import android.view.Menu;

import ru.droogcompanii.application.util.ui.able_to_start_task.ActivityAbleToStartTask;

/**
 * Created by ls on 14.02.14.
 */
public abstract class ActivityMenuHelper extends ActivityAbleToStartTask {

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
