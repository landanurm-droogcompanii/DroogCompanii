package ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper;

import android.app.Activity;
import android.support.v4.view.MenuItemCompat;

import ru.droogcompanii.application.util.Predicate;

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

    private static final Predicate ALWAYS_ENABLE_PREDICATE = new Predicate() {
        @Override
        public boolean isTrue() {
            return true;
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
    private Predicate predicateEnable;

    MenuItemHelper() {
        showAsAction = MenuItemCompat.SHOW_AS_ACTION_NEVER;
        predicateEnable = ALWAYS_ENABLE_PREDICATE;
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

    public MenuItemHelper withPredicateEnable(Predicate predicateEnable) {
        this.predicateEnable = predicateEnable;
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

    public boolean isEnable() {
        return predicateEnable.isTrue();
    }

}
