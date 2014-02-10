package ru.droogcompanii.application.ui.activity.menu;

import android.content.Context;

/**
 * Created by ls on 31.01.14.
 */
public class MenuListItem {
    public static interface Action {
        void launch(Context context);
    }

    public final int titleId;
    public final Action action;

    public MenuListItem(int titleId, Action action) {
        this.titleId = titleId;
        this.action = action;
    }
}
