package ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper;

import android.app.Activity;
import android.support.v4.view.MenuItemCompat;

/**
 * Created by ls on 14.02.14.
 */
public class MenuItemHelper {

    private static final Action DUMMY_ACTION = new Action() {
        @Override
        public void run(Activity activity) {
            // do nothing
        }
    };

    public static interface Action {
        void run(Activity activity);
    }

    private Action action = DUMMY_ACTION;
    private int id;
    private int order;
    private int titleId;
    private int iconId;
    private int showAsAction;

    MenuItemHelper() {
        showAsAction = MenuItemCompat.SHOW_AS_ACTION_NEVER;
    }

    MenuItemHelper withId(int id) {
        this.id = id;
        return this;
    }

    MenuItemHelper withTitleId(int titleId) {
        this.titleId = titleId;
        return this;
    }

    MenuItemHelper withOrder(int order) {
        this.order = order;
        return this;
    }

    public MenuItemHelper withAction(Action action) {
        this.action = action;
        return this;
    }

    MenuItemHelper withIcon(int iconId) {
        this.iconId = iconId;
        return this;
    }

    MenuItemHelper withShowAsAction(int showAsAction) {
        this.showAsAction = showAsAction;
        return this;
    }

    public Action getAction() {
        return action;
    }

    public int getId() {
        return id;
    }

    public int getOrder() {
        return order;
    }

    public int getTitleId() {
        return titleId;
    }

    public int getIconId() {
        return iconId;
    }

    public int getShowAsAction() {
        return showAsAction;
    }

}
